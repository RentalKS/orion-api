package com.orion.service;

import com.orion.dto.reservation.ReservationDto;
import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.generics.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.repository.*;
import com.orion.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleService extends BaseService {

    private final VehicleRepository vehicleRepository;
    private final TenantRepository tenantRepository;
    private final RateDatesRepository rateDatesRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final RentalRepository rentalRepository;
    private final ModelRepository modelRepository;
    private final LocationRepository locationRepository;

    public ResponseObject createVehicle(VehicleDto vehicleDto) {
        String methodName = "createVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Vehicle vehicle = new Vehicle();
                Optional<RateDates> rateDates = rateDatesRepository.findRateById(vehicleDto.getRateId(), tenant.get().getId());
        isPresent(rateDates);
        vehicle.setRateDates(rateDates.get());

        vehicleParameters(vehicleDto, responseObject, tenant, vehicle);
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    private void vehicleParameters(VehicleDto vehicleDto, ResponseObject responseObject, Optional<Tenant> tenant, Vehicle vehicle) {

        vehicleAttributes(vehicleDto, vehicle);
        Optional<RateDates> rateDates = rateDatesRepository.findRateById(vehicleDto.getRateId(), tenant.get().getId());
        isPresent(rateDates);
        vehicle.setRateDates(rateDates.get());

        vehicle.setTenant(tenant.get());

        responseObject.setData(vehicleRepository.save(vehicle));
    }

    private void vehicleAttributes(VehicleDto vehicleDto, Vehicle vehicle) {
        vehicle.setRegistrationNumber(vehicleDto.getRegistrationNumber());

        Optional<Model> model = modelRepository.findModelById(vehicleDto.getModelId(),TenantContext.getCurrentTenant().getId());

        isPresent(model);
        vehicle.setModel(model.get());
        vehicle.setYear(vehicleDto.getYear());
        vehicle.setStatus(vehicleDto.getStatus());
        vehicle.setFuelType(vehicleDto.getFuelType());
        vehicle.setMileage(vehicleDto.getMileage());
        vehicle.setTransmission(vehicleDto.getTransmission());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setDescription(vehicleDto.getDescription());
        vehicle.setImage(vehicleDto.getImage());

        Optional<Location> location = locationRepository.findLocationById(vehicleDto.getLocationId());
        isPresent(location);
        vehicle.setLocation(location.get());
    }

    public ResponseObject getVehicle(Long vehicleId) {
        String methodName = "getVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<VehicleDto> vehicle = vehicleRepository.findVehicleByIdFromDto(vehicleId, tenant.get().getId());
        isPresent(vehicle);

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAllVehicles() {
        String methodName = "getAllVehicles";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        responseObject.setData(vehicleRepository.findAllVehicles(tenant.get().getId()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        String methodName = "updateVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);

        Vehicle vehicleToUpdate = vehicle.get();
        vehicleParameters(vehicleDto, responseObject, tenant, vehicleToUpdate);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteVehicle(Long vehicleId) {
        String methodName = "deleteVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);

        vehicle.get().setDeletedAt(LocalDateTime.now());
        vehicleRepository.save(vehicle.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    @Transactional
    public ResponseObject createReservation(ReservationDto reservationDto) {
        String methodName = "createReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Vehicle> vehicle = vehicleRepository.findVehicleById(reservationDto.getVehicleId());
        isPresent(vehicle);

        Optional<Customer> customer = customerRepository.findCustomerByIdAndTenantId(reservationDto.getCustomerId(), tenant.get().getId());
        isPresent(customer);
        LocalDateTime startDate =  DateUtil.convertToLocalDateTime(reservationDto.getStartDate());
        LocalDateTime endDate = DateUtil.convertToLocalDateTime(reservationDto.getEndDate());

        boolean isAvailable = checkVehicleAvailability(vehicle.get(),startDate,endDate) ;

        if (!isAvailable) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            responseObject.setData("Vehicle is not available for the selected dates");
            return responseObject;
        }

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
        double totalCost = calculateTotalCost(vehicle.get().getRateDates(), rentalDays);

        Booking booking = new Booking();
        booking.setVehicle(vehicle.get());
        booking.setCustomer(customer.get());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus(RentalStatus.PENDING);
        booking.setVehicleStatus(VehicleStatus.RESERVED);
        booking.setTenant(tenant.get());

        Rental rental = new Rental();
        rental.setVehicle(vehicle.get());
        rental.setCustomer(customer.get());
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setStatus(RentalStatus.PENDING);
        rental.setTenant(tenant.get());
        rental.setTotalCost(totalCost);
        rental.setBooking(booking);

        rentalRepository.save(rental);
        bookingRepository.save(booking);

        vehicle.get().setStatus(VehicleStatus.RESERVED);
        vehicleRepository.save(vehicle.get());

        responseObject.setData(booking);
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        return responseObject;
    }

    private boolean checkVehicleAvailability(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {

        List<Booking> bookings = bookingRepository.findBookingsByVehicleAndDateRange(vehicle.getId(), startDate, endDate);
        return bookings.isEmpty();
    }

    private double calculateTotalCost(RateDates rateDates, long rentalDays) {
        double dailyRate = rateDates.getDailyRate();
        double weeklyRate = rateDates.getWeeklyRate();
        double monthlyRate = rateDates.getMonthlyRate();
        double totalCost;

        // Apply weekly rate if rentalDays is more than 7 and less than 30
        if (rentalDays >= 7 && rentalDays < 30) {
            totalCost = weeklyRate * ((double) rentalDays / 7);
        }
        // Apply monthly rate if rentalDays is more than 30
        else if (rentalDays >= 30) {
            totalCost = monthlyRate * ((double) rentalDays / 30);
        }
        // Otherwise, apply daily rate
        else {
            totalCost = dailyRate * rentalDays;
        }

        // Apply discounts if applicable
        if (rentalDays >= 14) {
            totalCost = applyDiscounts(totalCost, rentalDays);
        }
        return totalCost;
    }

    private double applyDiscounts(double totalCost, long rentalDays) {
        if (rentalDays > 14) {
            totalCost *= 0.90;
        }
        return totalCost;
    }

    public ResponseObject updateVehicleStatus(Long vehicleId, VehicleStatus status) {
        String methodName = "updateVehicleStatus";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);

        vehicle.get().setStatus(status);
        vehicleRepository.save(vehicle.get());

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateRentalStatuses() {
        log.info("Scheduled task: Updating rental statuses based on end dates.");
        List<Booking> bookings = bookingRepository.findByStatus(RentalStatus.ONGOING);

        bookings.forEach(booking -> {
            if (booking.getEndDate().isBefore(LocalDateTime.now())) {
                booking.setStatus(RentalStatus.COMPLETED);
                booking.getVehicle().setStatus(VehicleStatus.AVAILABLE);
                bookingRepository.save(booking);
                vehicleRepository.save(booking.getVehicle());
            }
        });
    }

    @Transactional
    public ResponseObject cancelReservation(Long bookingId) {
        String methodName = "cancelReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        isPresent(booking);

        Booking existingBooking = booking.get();
        if (existingBooking.getStatus() != RentalStatus.PENDING) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            responseObject.setData("Only pending reservations can be cancelled.");
            return responseObject;
        }

        existingBooking.setStatus(RentalStatus.CANCELED);
        bookingRepository.save(existingBooking);

        Vehicle vehicle = existingBooking.getVehicle();
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        responseObject.setData(existingBooking);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject checkAvailability(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        String methodName = "checkAvailability";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);

        boolean isAvailable = checkVehicleAvailability(vehicle.get(), startDate, endDate);
        responseObject.setData(isAvailable);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

}

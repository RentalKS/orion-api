package com.orion.service;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.generics.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.repository.*;
import com.orion.repository.nativeQuery.NativeQueryRepository;
import com.orion.service.BookingService.BookingService;
import com.orion.service.RentalService.RateDateService;
import com.orion.service.RentalService.RentalService;
import com.orion.service.UserService.TenantService;
import com.orion.service.UserService.UserService;
import com.orion.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleService extends BaseService {

    private final VehicleRepository vehicleRepository;

    private final TenantService tenantService;
    private final NotificationService notificationService;
    private final NativeQueryRepository nativeQueryRepository;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RentalService rentalService;
    private final RateDateService rentDateService;
    private final UserService userService;
    private final RateDateService rateDateService;
    private final ModelService modelService;
    private final LocationService locationService;

    public ResponseObject createVehicle(VehicleDto vehicleDto) {
        String methodName = "createVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findTenantById(TenantContext.getCurrentTenant().getId());
        RateDates rateDates = rentDateService.findRateDateById(vehicleDto.getRateId());

        Vehicle vehicle = new Vehicle();
        vehicle.setRateDates(rateDates);
        vehicleParameters(vehicleDto, responseObject, tenant, vehicle);

        User user = userService.findByEmail(vehicle.getCreatedBy());
        vehicle.setUser(user);
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        return responseObject;
    }
    public ResponseObject getVehicle(Long vehicleId) {
        String methodName = "getVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findTenantById(TenantContext.getCurrentTenant().getId());

        Optional<VehicleDto> vehicle = vehicleRepository.findVehicleByIdFromDto(vehicleId, tenant.getId());
        isPresent(vehicle);

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject getAllVehicles() {
        String methodName = "getAllVehicles";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findTenantById(TenantContext.getCurrentTenant().getId());


        responseObject.setData(vehicleRepository.findAllVehicles(tenant.getId()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject filterVehicles(Integer page, Integer size, VehicleFilter vehicleFilter){
        String methodName = "getAll";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        try {
            Long tenantId = TenantContext.getCurrentTenant().getId();
            List<Long> memberIds = new ArrayList<>();
            if (vehicleFilter.getAgencyId() != null) {
                List<Long> agencyMembers = userService.getAgencyMembers(vehicleFilter.getAgencyId());
                memberIds.addAll(agencyMembers);
            }

            String memberIdList = null;
            if (!memberIds.isEmpty())
                memberIdList = memberIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",", "(", ")"));

            List<VehicleDto> vehicleList = nativeQueryRepository.filterVehicles(tenantId, page, size, vehicleFilter, memberIdList);
            Long count = nativeQueryRepository.countVehicles(tenantId, vehicleFilter, memberIdList);

            Pageable pageable = PageRequest.of(page - 1, size);
            Page<VehicleDto> vehiclePage = new PageImpl<>(vehicleList, pageable, count);

            responseObject.setData(mapPage(vehiclePage));
            responseObject.prepareHttpStatus(HttpStatus.OK);
        }catch (Exception e) {
            log.error("Error getting vehicles: {}", e.getMessage());
            responseObject.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseObject.setData("An error occurred while getting vehicles.");
        }
        return responseObject;
    }
    public Vehicle findById(Long vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);
        return vehicle.get();
    }
    public ResponseObject updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        String methodName = "updateVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findTenantById(TenantContext.getCurrentTenant().getId());

        Vehicle vehicle = findById(vehicleId);

        vehicleParameters(vehicleDto, responseObject, tenant, vehicle);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject deleteVehicle(Long vehicleId) {
        String methodName = "deleteVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Vehicle vehicle = findById(vehicleId);

        vehicle.setDeletedAt(LocalDateTime.now());
        vehicleRepository.save(vehicle);
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public Vehicle findVehicleById(Long vehicleId) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findVehicleById(vehicleId);
        isPresent(optionalVehicle);
        return optionalVehicle.get();
    }
    @Transactional
    public ResponseObject createReservation(ReservationDto reservationDto) {
        String methodName = "createReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findTenantById(TenantContext.getCurrentTenant().getId());
        Vehicle vehicle = findVehicleById(reservationDto.getVehicleId());


        LocalDateTime startDate =  DateUtil.convertToLocalDateTime(reservationDto.getStartDate());
        LocalDateTime endDate = DateUtil.convertToLocalDateTime(reservationDto.getEndDate());
        Customer customer = customerService.findCustomerById(reservationDto.getCustomerId());

        boolean isAvailable = checkVehicleAvailability(vehicle,startDate,endDate) ;

        if (!isAvailable) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            ThrowException.throwBadRequestApiException(ErrorCode.VEHICLE_NOT_AVAILABLE, List.of("Vehicle is not available for the selected dates."));
            return responseObject;
        }
        try {

            long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
            double totalCost = calculateTotalCost(vehicle.getRateDates(), rentalDays);

            Booking booking = bookingService.createBooking(vehicle, customer, startDate, endDate, tenant);
            rentalService.createRental(vehicle, customer, startDate, endDate, tenant, totalCost, booking);

            vehicle.setStatus(VehicleStatus.RESERVED);
            vehicleRepository.save(vehicle);

            notificationService.sendNotification(customer.getId(), "Reservation Confirmation", "Your reservation has been confirmed.");

            responseObject.setData(booking);
            responseObject.prepareHttpStatus(HttpStatus.CREATED);
        }catch (Exception e) {
            log.error("Error creating reservation: {}", e.getMessage());
            responseObject.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseObject.setData("An error occurred while creating the reservation.");
        }
        return responseObject;
    }
    private boolean checkVehicleAvailability(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = bookingService.findBookingsByVehicleAndDateRange(vehicle.getId(), startDate, endDate);
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

        Vehicle vehicle = findById(vehicleId);
        vehicle.setStatus(status);
        vehicleRepository.save(vehicle);
        responseObject.setData(vehicle);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    @Transactional
    public ResponseObject cancelReservation(Long bookingId) {
        String methodName = "cancelReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Booking booking = bookingService.findBookingById(bookingId);

        if (booking.getStatus() != RentalStatus.PENDING) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            responseObject.setData("Only pending reservations can be cancelled.");
            return responseObject;
        }

        bookingService.updateBookingStatus(booking, RentalStatus.CANCELLED);

        Vehicle vehicle = booking.getVehicle();
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        responseObject.setData(booking);
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
    private void vehicleParameters(VehicleDto vehicleDto, ResponseObject responseObject, Tenant tenant, Vehicle vehicle) {

        vehicleAttributes(vehicleDto, vehicle);
        RateDates rateDates = rateDateService.findRateDateById(vehicleDto.getRateId());
        vehicle.setRateDates(rateDates);
        vehicle.setTenant(tenant);

        responseObject.setData(vehicleRepository.save(vehicle));
    }
    private void vehicleAttributes(VehicleDto vehicleDto, Vehicle vehicle) {
        vehicle.setRegistrationNumber(vehicleDto.getRegistrationNumber());

        Model model = modelService.findModelById(vehicleDto.getModelId());

        vehicle.setModel(model);
        vehicle.setYear(vehicleDto.getYear());
        vehicle.setStatus(vehicleDto.getStatus());
        vehicle.setFuelType(vehicleDto.getFuelType());
        vehicle.setMileage(vehicleDto.getMileage());
        vehicle.setTransmission(vehicleDto.getTransmission());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setDescription(vehicleDto.getDescription());
        vehicle.setImage(vehicleDto.getImage());

        Location location = locationService.findLocationById(vehicleDto.getLocationId());
        vehicle.setLocation(location);
    }
}

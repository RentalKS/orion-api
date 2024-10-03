package com.orion.service.vehicle;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.mapper.VehicleMapper;
import com.orion.repository.*;
import com.orion.repository.nativeQuery.NativeQueryRepository;
import com.orion.service.BaseService;
import com.orion.service.bookingService.BookingService;
import com.orion.service.notification.NotificationService;
import com.orion.service.rental.RateDatesService;
import com.orion.service.rental.RentalService;
import com.orion.service.user.TenantService;
import com.orion.service.user.UserService;
import com.orion.service.customer.CustomerService;
import com.orion.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleService extends BaseService {

    private final VehicleRepository repository;
    private final TenantService tenantService;
    private final NotificationService notificationService;
    private final NativeQueryRepository nativeQueryRepository;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RentalService rentalService;
    private final RateDatesService rateDateService;
    private final UserService userService;
    private final VehicleMapper vehicleMapper;

    public ResponseObject createVehicle(VehicleDto vehicleDto) {
        String methodName = "createVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();


        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto, new Vehicle());
        User user = userService.findByEmail(vehicle.getCreatedBy());
        vehicle.setUser(user);
        this.save(vehicle);

        responseObject.setData(vehicle.getId());
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        return responseObject;
    }
    public Vehicle findById(Long vehicleId) {
        Optional<Vehicle> vehicle = repository.findById(vehicleId);
        isPresent(vehicle);
        return vehicle.get();
    }
    public ResponseObject getVehicle(Long vehicleId) {
        String methodName = "getVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();

        Optional<VehicleDto> vehicle = repository.findVehicleByIdFromDto(vehicleId, tenant.getId());
        isPresent(vehicle);

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject getAllVehicles() {
        String methodName = "getAllVehicles";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        List<VehicleDto> vehicleDtoList = repository.findAllVehicles(ConfigSystem.getTenant().getId());
        responseObject.setData(Optional.of(vehicleDtoList).orElse(Collections.emptyList()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject filterVehicles(Integer page, Integer size, VehicleFilter vehicleFilter){
        String methodName = "getAll";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        try {
            Long tenantId = ConfigSystem.getTenant().getId();
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
    public ResponseObject updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        String methodName = "updateVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Vehicle vehicle = findById(vehicleId);
        vehicle = vehicleMapper.toEntity(vehicleDto, vehicle);
        repository.save(vehicle);

        responseObject.setData(vehicle.getId());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject deleteVehicle(Long vehicleId) {
        String methodName = "deleteVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Vehicle vehicle = findById(vehicleId);
        vehicle.setDeletedAt(LocalDateTime.now());
        this.save(vehicle);

        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    @Transactional
    public ResponseObject createReservation(ReservationDto reservationDto) {
        String methodName = "createReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();
        Vehicle vehicle = findById(reservationDto.getVehicleId());

        LocalDateTime startDate =  DateUtil.convertToLocalDateTime(reservationDto.getStartDate());
        LocalDateTime endDate = DateUtil.convertToLocalDateTime(reservationDto.getEndDate());
        Customer customer = customerService.findById(reservationDto.getCustomerId());

        boolean isNotAvailable = checkVehicleAvailability(vehicle,startDate,endDate) ;

        if (isNotAvailable) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            ThrowException.throwBadRequestApiException(ErrorCode.VEHICLE_NOT_AVAILABLE, List.of("Vehicle is not available for the selected dates."));
            return responseObject;
        }

        try {

            long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
            double totalCost = calculateTotalCost(vehicle.getRateDates(), rentalDays);

            Booking booking = bookingService.createBooking(vehicle, customer, startDate, endDate, tenant, VehicleStatus.RESERVED);
            rentalService.createRental(vehicle, customer, startDate, endDate, tenant, totalCost, booking,RentalStatus.PENDING,VehicleStatus.RESERVED);

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
        return bookingService.findBookingsByVehicleAndDateRange(vehicle.getId(), startDate, endDate);
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

        bookingService.updateBookingStatus(booking, RentalStatus.CANCELLED, VehicleStatus.AVAILABLE);
        Rental rental = rentalService.findByBooking(booking);
        rentalService.updateRentalStatus(rental, RentalStatus.CANCELLED, VehicleStatus.AVAILABLE);


        responseObject.setData(booking);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject checkAvailability(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        String methodName = "checkAvailability";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = repository.findById(vehicleId);
        isPresent(vehicle);

        boolean isNotAvailable = checkVehicleAvailability(vehicle.get(), startDate, endDate);
        responseObject.setData(isNotAvailable);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public Vehicle save(Vehicle vehicle) {
        try {
            return this.repository.save(vehicle);
        } catch (Exception e) {
            log.error("Error saving vehicle: {}", e.getMessage());
            throw new RuntimeException("Error saving vehicle.");
        }
    }

}

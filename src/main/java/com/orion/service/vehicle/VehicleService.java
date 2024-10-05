package com.orion.service.vehicle;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.entity.*;
import com.orion.enums.vehicle.DamageType;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.vehicle.VehicleViewDto;
import com.orion.mapper.VehicleMapper;
import com.orion.repository.*;
import com.orion.repository.nativeQuery.NativeQueryRepository;
import com.orion.service.BaseService;
import com.orion.service.bookingService.BookingService;
import com.orion.service.notification.NotificationService;
import com.orion.service.policyVehicle.InsurancePolicyService;
import com.orion.service.rental.RentalService;
import com.orion.service.user.TenantService;
import com.orion.service.user.UserService;
import com.orion.service.customer.CustomerService;
import com.orion.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleService extends BaseService {
    private final VehicleRepository repository;
    private final TenantService tenantService;
    private final NativeQueryRepository nativeQueryRepository;
    private final UserService userService;
    private final VehicleMapper vehicleMapper;
    private final InsurancePolicyService insurancePolicyService;

    public ResponseObject create(VehicleDto vehicleDto, String email) {
        String methodName = "createVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto, new Vehicle());
        User user = userService.findByEmail(email);
        vehicle.setUser(user);
        this.save(vehicle);

        InsurancePolicy insurancePolicy = insurancePolicyService.createFromVehicle(vehicle, vehicleDto.getInsurancePolicyDto());
        vehicle.setInsurancePolicy(insurancePolicy);
        this.save(vehicle);

        responseObject.setData(vehicle.getId());
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        return responseObject;
    }
    public Vehicle findById(Long vehicleId) {
        Optional<Vehicle> vehicle = repository.findVehicleById(vehicleId,ConfigSystem.getTenant().getId());
        isPresent(vehicle);
        return vehicle.get();
    }
    public ResponseObject get(Long vehicleId) {
        String methodName = "getVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();

        Optional<VehicleViewDto> vehicle = repository.findVehicleDto(vehicleId, tenant.getId());
        isPresent(vehicle);

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject getAll(String currentEmail, Integer page, Integer size) {
        String methodName = "getAllVehicles";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Pageable pageable = PageRequest.of(
                page != null ? page - 1 : 1,
                size != null ? size : 10,
                Sort.by("id").descending());

        Page<VehicleViewDto> vehicleViewDtoList = repository.findAllVehicles(ConfigSystem.getTenant().getId(),currentEmail, pageable);

        responseObject.setData(mapPage(vehicleViewDtoList));
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

            List<VehicleViewDto> vehicleList = nativeQueryRepository.filterVehicles(tenantId, page, size, vehicleFilter, memberIdList);
            Long count = nativeQueryRepository.countVehicles(tenantId, vehicleFilter, memberIdList);

            Pageable pageable = PageRequest.of(page - 1, size);
            Page<VehicleViewDto> vehiclePage = new PageImpl<>(vehicleList, pageable, count);

            responseObject.setData(mapPage(vehiclePage));
            responseObject.prepareHttpStatus(HttpStatus.OK);
        }catch (Exception e) {
            log.error("Error getting vehicles: {}", e.getMessage());
            responseObject.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseObject.setData("An error occurred while getting vehicles.");
        }
        return responseObject;
    }
    public ResponseObject update(Long vehicleId, VehicleDto vehicleUpdateDto) {
        String methodName = "updateVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Vehicle vehicle = findById(vehicleId);
        vehicle = vehicleMapper.toEntity(vehicleUpdateDto, vehicle);
        repository.save(vehicle);

        responseObject.setData(vehicle.getId());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject delete(Long vehicleId) {
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
    public Vehicle save(Vehicle vehicle) {
        try {
            return this.repository.save(vehicle);
        } catch (Exception e) {
            log.error("Error saving vehicle: {}", e.getMessage());
            throw new RuntimeException("Error saving vehicle.");
        }
    }
    public Boolean isVehicleOnMaintenance(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.isVehicleOnMaintenance(id, startDate, endDate);
    }
}

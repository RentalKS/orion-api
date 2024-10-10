package com.orion.service.vehicle;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.entity.*;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.vehicle.VehicleViewDto;
import com.orion.mapper.VehicleMapper;
import com.orion.repository.*;
import com.orion.service.BaseService;
import com.orion.service.policyVehicle.InsurancePolicyService;
import com.orion.service.user.TenantService;
import com.orion.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleService extends BaseService {
    private final VehicleRepository repository;
    private final TenantService tenantService;
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

        InsurancePolicy insurancePolicy = insurancePolicyService.createFromVehicle(vehicle, vehicleDto.getInsurancePolicyList());
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
        Optional<VehicleViewDto> vehicle = repository.findVehicleDto(vehicleId, ConfigSystem.getTenant().getId());
        List<MaintenanceRecordDto> maintenanceRecord = repository.findMaintenanceRecord(vehicleId);
        vehicle.get().setMaintenanceRecord(Optional.ofNullable(maintenanceRecord).orElse(List.of()));
        isPresent(vehicle);

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject getAll(VehicleFilter vehicleFilter,String currentEmail, int page, int size,String search) {
        String methodName = "getAllVehicles";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Pageable pageable = PageRequest.of(page -1, size,
                Sort.by("id").descending());
        User user = userService.findByEmail(currentEmail);

        List<Long> userIds = userService.getUserIdsBasedOnRole(user);

        Page<VehicleViewDto> vehicleViewDtoList =
                repository.findAllVehicles(
                        ConfigSystem.getTenant().getId(),
                        userIds,
                        vehicleFilter.getFrom(),
                        vehicleFilter.getTo(),
                        vehicleFilter.getLocationId(),
                        vehicleFilter.getCompanyId(),
                        vehicleFilter.getCategoryId(),
                        vehicleFilter.getSectionId(),
                        vehicleFilter.getStatus(),
                        search,
                        pageable);
        responseObject.setData(mapPage(vehicleViewDtoList));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
//    public ResponseObject filterVehicles(Integer page, Integer size, VehicleFilter vehicleFilter){
//        String methodName = "getAll";
//        log.info("Entering: {}", methodName);
//        ResponseObject responseObject = new ResponseObject();
//
//        try {
//            Long tenantId = ConfigSystem.getTenant().getId();
//            List<Long> memberIds = new ArrayList<>();
//
//            if (vehicleFilter.getAgencyId() != null) {
//                User agency = userService.findById(vehicleFilter.getAgencyId());
//                List<Long> customersOfCurrentAgency = customerService.findCustomerIdsFromAgencies(Collections.singletonList(agency.getEmail()));
//                memberIds.addAll(customersOfCurrentAgency);
//            }
//
//            String memberIdList = null;
//            if (!memberIds.isEmpty())
//                memberIdList = memberIds.stream()
//                        .map(String::valueOf)
//                        .collect(Collectors.joining(",", "(", ")"));
//
//            List<VehicleViewDto> vehicleList = nativeQueryRepository.filterVehicles(tenantId, page, size, vehicleFilter, memberIdList);
//            Long count = nativeQueryRepository.countVehicles(tenantId, vehicleFilter, memberIdList);
//
//            Pageable pageable = PageRequest.of(page - 1, size);
//            Page<VehicleViewDto> vehiclePage = new PageImpl<>(vehicleList, pageable, count);
//
//            responseObject.setData(mapPage(vehiclePage));
//            responseObject.prepareHttpStatus(HttpStatus.OK);
//        }catch (Exception e) {
//            log.error("Error getting vehicles: {}", e.getMessage());
//            responseObject.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            responseObject.setData("An error occurred while getting vehicles.");
//        }
//        return responseObject;
//    }
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

    public List<VehicleDto> findVehiclesFromModel(Long modelId) {
        List<VehicleDto> vehicleDtoList= repository.findVehiclesFromModel(modelId,ConfigSystem.getTenant().getId());
        if(vehicleDtoList.isEmpty()){
            return List.of();
        }
        return vehicleDtoList;
    }

    public List<VehicleDto> findVehiclesFromSection(Long sectionId) {
        List<VehicleDto> vehicleDtoList= repository.findVehiclesFromSection(sectionId,ConfigSystem.getTenant().getId());
        if(vehicleDtoList.isEmpty()){
            return List.of();
        }
        return vehicleDtoList;
    }
}

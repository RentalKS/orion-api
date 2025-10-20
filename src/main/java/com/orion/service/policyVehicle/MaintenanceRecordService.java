package com.orion.service.policyVehicle;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.entity.MaintenanceRecord;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.repository.MaintenanceRecordRepository;
import com.orion.repository.TenantRepository;
import com.orion.repository.VehicleRepository;
import com.orion.service.BaseService;
import com.orion.service.user.TenantService;
import com.orion.service.vehicle.VehicleService;
import com.orion.util.DateUtil;
import com.orion.util.DtoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaintenanceRecordService extends BaseService {
    private final TenantService tenantService;
    private final MaintenanceRecordRepository repository;
    private final VehicleService vehicleService;

    public ResponseObject create(MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "createMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Vehicle vehicle = vehicleService.findById(maintenanceRecordDto.getVehicleId());
        Tenant tenant = tenantService.findById();

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setMaintenanceStartDate(DateUtil.convertToLocalDateTime(maintenanceRecordDto.getMaintenanceStartDate()));
        maintenanceRecord.setMaintenanceEndDate(maintenanceRecordDto.getMaintenanceEndDate() != null ? DateUtil.convertToLocalDateTime(maintenanceRecordDto.getMaintenanceEndDate()) : null);
        maintenanceRecord.setDescription(maintenanceRecordDto.getDescription());
        maintenanceRecord.setCost(maintenanceRecordDto.getCost());
        maintenanceRecord.setVehicle(vehicle);
        maintenanceRecord.setDamageType(maintenanceRecordDto.getDamageType());
        maintenanceRecord.setTenant(tenant);

        responseObject.setData(repository.save(maintenanceRecord));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject get(Long maintenanceRecordId) {
        String methodName = "getMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<MaintenanceRecordDto> maintenanceRecord = repository.findByIdDto(maintenanceRecordId,ConfigSystem.getTenant().getId());
        isPresent(maintenanceRecord);

        responseObject.setData(maintenanceRecord);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject update(Long updateId,MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "updateMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<MaintenanceRecord> maintenanceRecord = repository.findById(updateId);
        isPresent(maintenanceRecord);

        MaintenanceRecord maintenanceRecordToUpdate = maintenanceRecord.get();
        if(maintenanceRecordDto.getMaintenanceStartDate() != null){
            maintenanceRecordToUpdate.setMaintenanceStartDate(DateUtil.convertToLocalDateTime(maintenanceRecordDto.getMaintenanceStartDate()));
        }else {
            maintenanceRecordToUpdate.setMaintenanceStartDate(null);
        }
        if(maintenanceRecordDto.getMaintenanceEndDate() != null) {
            maintenanceRecordToUpdate.setMaintenanceEndDate(DateUtil.convertToLocalDateTime(maintenanceRecordDto.getMaintenanceEndDate()));
        }else {
            maintenanceRecordToUpdate.setMaintenanceEndDate(null);
        }

        DtoUtils.setIfNotNull(maintenanceRecordDto.getDescription(), maintenanceRecordToUpdate::setDescription);
        DtoUtils.setIfNotNull(maintenanceRecordDto.getCost(), maintenanceRecordToUpdate::setCost);
        DtoUtils.setIfNotNull(maintenanceRecordDto.getDamageType(), maintenanceRecordToUpdate::setDamageType);

        responseObject.setData(repository.save(maintenanceRecordToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject delete(Long maintenanceRecordId) {
        String methodName = "deleteMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        MaintenanceRecord maintenanceRecord = findById(maintenanceRecordId);
        maintenanceRecord.setDeletedAt(LocalDateTime.now());
        repository.save(maintenanceRecord);

        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject getAll(String currentEmail) {
        String methodName = "getAllMaintenanceRecords";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        List<MaintenanceRecordDto> maintenanceRecordDtoList =repository.findAllByTenant(currentEmail,ConfigSystem.getTenant().getId());
        responseObject.setData(Optional.of(maintenanceRecordDtoList).orElse(List.of()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public MaintenanceRecord findById(Long maintenanceRecordId){
        Optional<MaintenanceRecord> optionalMaintenanceRecord = repository.findById(maintenanceRecordId, ConfigSystem.getTenant().getId());
        isPresent(optionalMaintenanceRecord);
        return optionalMaintenanceRecord.get();
    }
}

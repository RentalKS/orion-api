package com.orion.service;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.TenantContext;
import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.entity.MaintenanceRecord;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.repository.MaintenanceRecordRepository;
import com.orion.repository.TenantRepository;
import com.orion.repository.VehicleRepository;
import com.orion.util.DtoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaintenanceRecordService extends BaseService {
    private final TenantRepository tenantRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final VehicleRepository vehicleRepository;

    public ResponseObject createMaintenanceRecord(MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "createMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(maintenanceRecordDto.getVehicleId());
        isPresent(vehicle);
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setMaintenanceDate(maintenanceRecordDto.getMaintenanceDate());
        maintenanceRecord.setDescription(maintenanceRecordDto.getDescription());
        maintenanceRecord.setCost(maintenanceRecordDto.getCost());
        maintenanceRecord.setVehicle(vehicle.get());
        maintenanceRecord.setTenant(tenant.get());

        responseObject.setData(maintenanceRecordRepository.save(maintenanceRecord));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getMaintenanceRecord(Long maintenanceRecordId) {
        String methodName = "getMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<MaintenanceRecord> maintenanceRecord = maintenanceRecordRepository.findById(maintenanceRecordId);
        isPresent(maintenanceRecord);

        responseObject.setData(maintenanceRecord.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateMaintenanceRecord(MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "updateMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<MaintenanceRecord> maintenanceRecord = maintenanceRecordRepository.findById(maintenanceRecordDto.getId());
        isPresent(maintenanceRecord);

        MaintenanceRecord maintenanceRecordToUpdate = maintenanceRecord.get();
        DtoUtils.setIfNotNull(maintenanceRecordDto.getMaintenanceDate(), maintenanceRecordToUpdate::setMaintenanceDate);
        DtoUtils.setIfNotNull(maintenanceRecordDto.getDescription(), maintenanceRecordToUpdate::setDescription);
        DtoUtils.setIfNotNull(maintenanceRecordDto.getCost(), maintenanceRecordToUpdate::setCost);

        responseObject.setData(maintenanceRecordRepository.save(maintenanceRecordToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteMaintenanceRecord(Long maintenanceRecordId) {
        String methodName = "deleteMaintenanceRecord";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<MaintenanceRecord> maintenanceRecord = maintenanceRecordRepository.findById(maintenanceRecordId);
        isPresent(maintenanceRecord);

        maintenanceRecordRepository.delete(maintenanceRecord.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}

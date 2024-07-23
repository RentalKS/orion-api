package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.entity.*;
import com.orion.repository.*;
import com.orion.util.DtoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleService extends BaseService {
    private final TenantRepository tenantRepository;
    private final VehicleRepository vehicleRepository;
    private final SectionRepository sectionRepository;
    private final FleetRepository fleetRepository;
    private final LocationRepository locationRepository;

    public ResponseObject createVehicle(VehicleDto vehicleDto) {
        String methodName = "createVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Section> section = sectionRepository.findById(vehicleDto.getSectionId());
        isPresent(section);

        Optional<Tenant> tenant =tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Fleet> fleet = fleetRepository.findById(vehicleDto.getFleetId());
        isPresent(fleet);

        Optional<Location> location = locationRepository.findById(vehicleDto.getLocationId());
        isPresent(location);

        Vehicle vehicle = new Vehicle();
        setVehicleProperties(vehicle, vehicleDto, section.get(), fleet.get(), location.get());
        vehicle.setTenant(tenant.get());

        responseObject.setData(vehicleRepository.save(vehicle));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }
    private void setVehicleProperties(Vehicle vehicle, VehicleDto vehicleDto, Section section, Fleet fleet, Location location) {
        DtoUtils.setIfNotNull(vehicleDto.getModel(), vehicle::setModel);
        DtoUtils.setIfNotNull(vehicleDto.getYear(), vehicle::setYear);
        DtoUtils.setIfNotNull(vehicleDto.getPrice(), vehicle::setPrice);
        DtoUtils.setIfNotNull(vehicleDto.getImage(), vehicle::setImage);
        DtoUtils.setIfNotNull(vehicleDto.getDescription(), vehicle::setDescription);
        DtoUtils.setIfNotNull(vehicleDto.getStatus(), vehicle::setStatus);
        DtoUtils.setIfNotNull(vehicleDto.getFuelType(), vehicle::setFuelType);
        DtoUtils.setIfNotNull(vehicleDto.getMileage(), vehicle::setMileage);
        DtoUtils.setIfNotNull(vehicleDto.getEngine(), vehicle::setEngine);
        DtoUtils.setIfNotNull(vehicleDto.getTransmission(), vehicle::setTransmission);
        DtoUtils.setIfNotNull(vehicleDto.getColor(), vehicle::setColor);
        DtoUtils.setIfNotNull(vehicleDto.getInterior(), vehicle::setInterior);
        DtoUtils.setIfNotNull(vehicleDto.getExterior(), vehicle::setExterior);
        DtoUtils.setIfNotNull(vehicleDto.getVin(), vehicle::setVin);
        DtoUtils.setIfNotNull(vehicleDto.getStockNumber(), vehicle::setStockNumber);
        DtoUtils.setIfNotNull(vehicleDto.getMpg(), vehicle::setMpg);
        DtoUtils.setIfNotNull(vehicleDto.getFeatures(), vehicle::setFeatures);
        DtoUtils.setIfNotNull(vehicleDto.getOptions(), vehicle::setOptions);
        DtoUtils.setIfNotNull(vehicleDto.getRegistrationNumber(), vehicle::setRegistrationNumber);

        vehicle.setSection(section);
        vehicle.setFleet(fleet);
        vehicle.setLocation(location);
    }

    public ResponseObject getVehicle(Long vehicleId) {
        String methodName = "getVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);

        responseObject.setData(vehicle.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateVehicle(VehicleDto vehicleDto) {
        String methodName = "updateVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleDto.getId());
        isPresent(vehicle);

        Vehicle vehicleToUpdate = vehicle.get();
        vehicleToUpdate.setModel(vehicleDto.getModel());
        vehicleToUpdate.setYear(vehicleDto.getYear());
        vehicleToUpdate.setPrice(vehicleDto.getPrice());
        vehicleToUpdate.setImage(vehicleDto.getImage());
        vehicleToUpdate.setDescription(vehicleDto.getDescription());
        vehicleToUpdate.setStatus(vehicleDto.getStatus());
        vehicleToUpdate.setFuelType(vehicleDto.getFuelType());
        vehicleToUpdate.setMileage(vehicleDto.getMileage());
        vehicleToUpdate.setEngine(vehicleDto.getEngine());
        vehicleToUpdate.setTransmission(vehicleDto.getTransmission());
        vehicleToUpdate.setColor(vehicleDto.getColor());
        vehicleToUpdate.setInterior(vehicleDto.getInterior());
        vehicleToUpdate.setExterior(vehicleDto.getExterior());
        vehicleToUpdate.setVin(vehicleDto.getVin());
        vehicleToUpdate.setStockNumber(vehicleDto.getStockNumber());
        vehicleToUpdate.setMpg(vehicleDto.getMpg());
        vehicleToUpdate.setFeatures(vehicleDto.getFeatures());
        vehicleToUpdate.setOptions(vehicleDto.getOptions());
        vehicleToUpdate.setRegistrationNumber(vehicleDto.getRegistrationNumber());

        Optional<Section> section = sectionRepository.findById(vehicleDto.getSectionId());
        isPresent(section);
        vehicleToUpdate.setSection(section.get());

        Optional<Fleet> fleet = fleetRepository.findById(vehicleDto.getFleetId());
        isPresent(fleet);
        vehicleToUpdate.setFleet(fleet.get());

        Optional<Location> location = locationRepository.findById(vehicleDto.getLocationId());
        isPresent(location);
        vehicleToUpdate.setLocation(location.get());

        responseObject.setData(vehicleRepository.save(vehicleToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteVehicle(Long vehicleId) {
        String methodName = "deleteVehicle";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);
        vehicle.get().setDeletedAt(LocalDateTime.now());

        vehicleRepository.save(vehicle.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}

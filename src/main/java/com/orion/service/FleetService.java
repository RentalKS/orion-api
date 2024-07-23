package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.dto.fleet.FleetDto;
import com.orion.entity.Fleet;
import com.orion.entity.Location;
import com.orion.entity.Tenant;
import com.orion.repository.FleetRepository;
import com.orion.repository.LocationRepository;
import com.orion.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class FleetService extends BaseService {
    private final TenantRepository tenantRepository;
    private final FleetRepository fleetRepository;
    private final LocationRepository locationRepository;

    public ResponseObject createFleet(FleetDto fleetDto) {
        String methodName = "createFleet";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Location> location = locationRepository.findById(fleetDto.getLocationId());
        isPresent(location);
        Optional<Tenant> tenant = tenantRepository.findTenantById(fleetDto.getLocationId());
        isPresent(tenant);

        Fleet fleet = new Fleet();
        fleet.setFleetName(fleetDto.getFleetName());
        fleet.setDescription(fleetDto.getDescription());
        fleet.setLocation(location.get());
        fleet.setTenant(tenant.get());

        responseObject.setData(fleetRepository.save(fleet));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getFleet(Long fleetId) {
        String methodName = "getFleet";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Fleet> fleet = fleetRepository.findById(fleetId);
        isPresent(fleet);

        responseObject.setData(fleet.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateFleet(FleetDto fleetDto) {
        String methodName = "updateFleet";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Fleet> fleet = fleetRepository.findById(fleetDto.getId());
        isPresent(fleet);

        Fleet fleetToUpdate = fleet.get();
        fleetToUpdate.setFleetName(fleetDto.getFleetName());
        fleetToUpdate.setDescription(fleetDto.getDescription());

        Optional<Location> location = locationRepository.findById(fleetDto.getLocationId());
        isPresent(location);
        fleetToUpdate.setLocation(location.get());

        responseObject.setData(fleetRepository.save(fleetToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteFleet(Long fleetId) {
        String methodName = "deleteFleet";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Fleet> fleet = fleetRepository.findById(fleetId);
        isPresent(fleet);

        fleetRepository.delete(fleet.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}

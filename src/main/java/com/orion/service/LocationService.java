package com.orion.service;

import com.orion.generics.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.location.LocationDto;
import com.orion.entity.Location;
import com.orion.entity.Tenant;
import com.orion.repository.LocationRepository;
import com.orion.repository.TenantRepository;
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
public class LocationService extends BaseService {
    private final TenantRepository tenantRepository;
    private final LocationRepository locationRepository;

    public ResponseObject createLocation(LocationDto locationDto) {
        String methodName = "createLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Location location = new Location();
        locationAttributes(locationDto, responseObject, location);
        location.setTenant(tenant.get());

        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    private void locationAttributes(LocationDto locationDto, ResponseObject responseObject, Location location) {
        DtoUtils.setIfNotNull(locationDto.getLocationName(), location::setLocationName);
        DtoUtils.setIfNotNull(locationDto.getAddress(), location::setAddress);
        DtoUtils.setIfNotNull(locationDto.getCity(), location::setCity);
        DtoUtils.setIfNotNull(locationDto.getState(), location::setState);
        DtoUtils.setIfNotNull(locationDto.getZipCode(), location::setZipCode);
        DtoUtils.setIfNotNull(locationDto.getCountry(), location::setCountry);
        responseObject.setData(locationRepository.save(location));
    }

    public ResponseObject getLocation(Long locationId) {
        String methodName = "getLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Location> location = locationRepository.findById(locationId);
        isPresent(location);

        responseObject.setData(location.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateLocation(Long locationId,LocationDto locationDto) {
        String methodName = "updateLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Location> location = locationRepository.findById(locationId);
        isPresent(location);

        Location locationToUpdate = location.get();
        locationAttributes(locationDto, responseObject, locationToUpdate);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteLocation(Long locationId) {
        String methodName = "deleteLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Location> location = locationRepository.findById(locationId);
        isPresent(location);
        location.get().setDeletedAt(LocalDateTime.now());

        locationRepository.save(location.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}
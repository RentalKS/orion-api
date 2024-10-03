package com.orion.service.location;

import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.location.LocationDto;
import com.orion.entity.Location;
import com.orion.entity.Tenant;
import com.orion.mapper.LocationMapper;
import com.orion.repository.LocationRepository;
import com.orion.service.BaseService;
import com.orion.service.user.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LocationService extends BaseService {
    private final TenantService tenantService;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    public Location findLocationById(Long locationId){
        Optional<Location> location = locationRepository.findLocationById(locationId,ConfigSystem.getTenant().getId());
        isPresent(location);
        return location.get();
    }
    public ResponseObject createLocation(LocationDto locationDto) {
        String methodName = "createLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();

        Location location = locationMapper.toEntity(locationDto,new Location());
        location.setTenant(tenant);
        locationRepository.save(location);

        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        responseObject.setData(location.getId());

        return responseObject;
    }

    public ResponseObject getLocation(Long locationId) {
        String methodName = "getLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<LocationDto> location = locationRepository.findLocationByIdAndByTenant(locationId,ConfigSystem.getTenant().getId());
        isPresent(location);

        responseObject.setData(location.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAllLocations(String currentEmail) {
        String methodName = "getAllLocations";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Long tenantId = ConfigSystem.getTenant().getId();
        List<LocationDto> locationDtoList = locationRepository.findAllLocationsByTenant(tenantId,currentEmail);
        responseObject.setData(Optional.ofNullable(locationDtoList).orElseGet(Collections::emptyList));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateLocation(Long locationId,LocationDto locationDto) {
        String methodName = "updateLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Location locationToUpdate = findLocationById(locationId);

        locationToUpdate = locationMapper.toEntity(locationDto, locationToUpdate);
        locationRepository.save(locationToUpdate);

        responseObject.prepareHttpStatus(HttpStatus.OK);
        responseObject.setData(locationToUpdate.getId());
        return responseObject;
    }

    public ResponseObject deleteLocation(Long locationId) {
        String methodName = "deleteLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Location locationToDelete = findLocationById(locationId);
        locationToDelete.setDeletedAt(LocalDateTime.now());

        locationRepository.save(locationToDelete);
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}
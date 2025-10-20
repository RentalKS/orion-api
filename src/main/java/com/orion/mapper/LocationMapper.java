package com.orion.mapper;

import com.orion.dto.location.LocationDto;
import com.orion.entity.Location;
import com.orion.generics.ResponseObject;
import com.orion.util.DtoUtils;
import org.springframework.stereotype.Service;

@Service
public class LocationMapper {
    public Location toEntity(LocationDto locationDto,Location location) {
        location.setTables(locationDto.getTables());
        location.setCity(locationDto.getCity());
        location.setState(locationDto.getState());
        location.setAddress(locationDto.getAddress());
        location.setZipCode(locationDto.getZipCode());
        location.setCountry(locationDto.getCountry());
        return location;
    }
}

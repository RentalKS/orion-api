package com.orion.dto.location;

import com.orion.entity.Location;
import com.orion.util.DateUtil;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Location}
 */
@Value
public class LocationDto implements Serializable {
    Long id;
    Long createdAt;
    String address;
    String city;
    String state;
    String zipCode;
    String country;
    String tables;

    public LocationDto(Long id, LocalDateTime createdAt, String address, String city, String state, String zipCode, String country, String tables) {
        this.id = id;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.tables = tables;
    }
}
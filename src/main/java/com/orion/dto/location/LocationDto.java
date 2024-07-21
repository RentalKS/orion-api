package com.orion.dto.location;

import com.orion.entity.Location;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Location}
 */
@Value
public class LocationDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    String locationName;
    String address;
    String city;
    String state;
    String zipCode;
    String country;
}
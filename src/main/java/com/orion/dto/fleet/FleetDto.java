package com.orion.dto.fleet;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.Fleet}
 */
@Value
public class FleetDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    String fleetName;
    String description;
    Long locationId;
}
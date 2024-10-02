package com.orion.dto.location;

import com.orion.entity.Location;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Location}
 */
@Getter
@Setter
@NoArgsConstructor
public class LocationDto implements Serializable {
    Long id;
    Long createdAt;
    String address;
    @NotBlank
    String city;
    @NotBlank
    String state;
    String zipCode;
    @NotBlank
    String country;
    @NotBlank
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
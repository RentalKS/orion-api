package com.orion.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleFilter {
    private Long from;

    private Long to;

    private String status;

    private Long userId;

    private Long locationId;

    private Long customerId;

    private String search;

    private Long agencyId;
}

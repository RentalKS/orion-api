package com.orion.dto.filter;

import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class VehicleFilter {
    private Long from;

    private Long to;

    private VehicleStatus status;

    private Long userId;

    private Long locationId;

    private Long companyId;

    private Long categoryId;

    private Long sectionId;

    private String search;

    public LocalDateTime getTo() {
        if(to == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(to);
    }

    public LocalDateTime getFrom() {
        if(from == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(from);
    }
}

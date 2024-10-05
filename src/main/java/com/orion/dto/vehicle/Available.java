package com.orion.dto.vehicle;

import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Available {
    @NotNull
    private Long vehicleId;
    private Long startDate;
    private Long endDate;

    public Available(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        this.vehicleId = vehicleId;
        this.startDate = startDate != null ? DateUtil.localDateTimeToMilliseconds(startDate) : null;
        this.endDate = endDate != null ? DateUtil.localDateTimeToMilliseconds(endDate) : null;
    }

    public LocalDateTime getStartDate() {
        if(startDate == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(startDate);
    }

    public LocalDateTime getEndDate() {
        if(endDate == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(endDate);
    }
}

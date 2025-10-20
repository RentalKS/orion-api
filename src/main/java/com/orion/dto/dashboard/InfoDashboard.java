package com.orion.dto.dashboard;

import com.orion.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoDashboard {
    private Long from;

    private Long to;

    public LocalDateTime getFrom() {
        if(from == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(from);
    }

    public LocalDateTime getTo() {
        if(to == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(to);
    }
}

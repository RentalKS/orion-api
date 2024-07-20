package com.orion.dto;

import com.orion.entity.Role;
import com.orion.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class UserData {
    private Long id;
    private String username;
    private String lastName;
    private String email;
    private Long createdAt;

    public UserData(Long id, String firstName, String lastName, String email, LocalDateTime createdAt) {
        this.id = id;
        this.username = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
    }

}

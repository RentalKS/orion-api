package com.orion.dto.user;

import com.orion.dto.company.CompanyDto;
import com.orion.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class UserData {
    private Long id;
    private String username;
    private String lastName;
    private String email;
    private Long createdAt;
    private String role;
    private List<CompanyDto> companies;

    public UserData(Long id, String firstName, String lastName, String email, LocalDateTime createdAt,String role) {
        this.id = id;
        this.username = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.role = role;
    }

}

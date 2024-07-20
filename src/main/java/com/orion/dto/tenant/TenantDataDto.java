package com.orion.dto.tenant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TenantDataDto {
    private Long id;

    private String name;

    private String domain;

    private String firstName;

    private String lastName;

    private String email;

    public TenantDataDto(Long id, String name, String domain, String firstName, String lastName, String email) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

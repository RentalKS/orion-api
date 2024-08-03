package com.orion.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String licenseNumber;

    public CustomerDto(Long id, String name, String email, String phoneNumber, String licenseNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
    }
}

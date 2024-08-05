package com.orion.dto.customer;

import com.orion.entity.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private List<Rental> rentals;

    public CustomerDto(Long id, String name, String email, String phoneNumber, String licenseNumber, List<Rental> rentals) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.rentals = rentals;
    }

        public CustomerDto(Long id, String name, String email, String phoneNumber, String licenseNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
    }
}

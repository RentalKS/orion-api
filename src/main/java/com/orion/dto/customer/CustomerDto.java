package com.orion.dto.customer;

import com.orion.dto.rental.RentalDto;
import com.orion.entity.Rental;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    private String licenseNumber;
    private String contactAgent;
    private List<RentalDto> rentals;

    public CustomerDto(Long id, String name, String lastName, String email, String phoneNumber, String licenseNumber, String contactAgent) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.contactAgent = contactAgent;
    }
}

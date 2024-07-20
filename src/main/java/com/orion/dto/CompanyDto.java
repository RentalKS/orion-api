package com.orion.dto;

import com.orion.entity.User;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.Company}
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private Long createdAt;
    private String createdBy;
    private Long id;
    @NotNull
    private String companyName;
    private String companyAddress;
    @NotNull
    @Email(message = "Invalid email format")
    private String companyEmail;
    private String companyPhone;
    private String companyLogo;
    private String zipCode;
    private String city;
    private String state;
    @NotNull
    private Integer userId;


    public CompanyDto(LocalDateTime createdAt, String createdBy, Long id, @NotNull String companyName, String companyAddress, @NotNull String companyEmail, String companyPhone, String companyLogo, String zipCode, String city, String state,@NotNull Integer userId) {
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.createdBy = createdBy;
        this.id = id;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyLogo = companyLogo;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.userId = userId;
    }
}
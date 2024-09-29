package com.orion.dto.company;

import com.orion.dto.category.CategoryDto;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private String name;
    private String address;
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
    private String phone;
    private MultipartFile logo;
    private String logoUrl;
    private String zipCode;
    private String city;
    private String state;
    @NotBlank(message = "choose user")
    private Long userId;
    private List<CategoryDto> categories;


    public CompanyDto(LocalDateTime createdAt, String createdBy, Long id, @NotNull String name, String address, @NotNull String email, String phone, String logoUrl, String zipCode, String city, String state,@NotNull Long userId) {
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.createdBy = createdBy;
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.logoUrl = logoUrl;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.userId = userId;
    }
}
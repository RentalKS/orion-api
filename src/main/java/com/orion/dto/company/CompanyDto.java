package com.orion.dto.company;

import com.orion.dto.category.CategoryDto;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.Email;
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
    private String companyName;
    private String companyAddress;
    @NotNull
    @Email(message = "Invalid email format")
    private String companyEmail;
    private String companyPhone;
    private MultipartFile companyLogo;
    private String companyLogoUrl;
    private String zipCode;
    private String city;
    private String state;
    @NotNull
    private Long userId;
    private List<CategoryDto> categories;


    public CompanyDto(LocalDateTime createdAt, String createdBy, Long id, @NotNull String companyName, String companyAddress, @NotNull String companyEmail, String companyPhone, String companyLogoUrl, String zipCode, String city, String state,@NotNull Long userId) {
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.createdBy = createdBy;
        this.id = id;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyLogoUrl = companyLogoUrl;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.userId = userId;
    }
}
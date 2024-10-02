package com.orion.dto.company;

import com.orion.dto.category.CategoryDto;
import com.orion.entity.Company;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MultipartFile getLogo() {
        return logo;
    }

    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
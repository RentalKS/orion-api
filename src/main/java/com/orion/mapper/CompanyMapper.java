package com.orion.mapper;

import com.orion.dto.company.CompanyDto;
import com.orion.entity.Company;
import com.orion.entity.Tenant;
import com.orion.entity.User;
import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.service.user.TenantService;
import com.orion.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CompanyMapper {
    private final UserService userService;
    private final TenantService tenantService;
    private final FileUploadService fileUploadService;

    public Company toEntity(CompanyDto companyDto, Company companyToUpdate) {
        Tenant tenant = tenantService.findById();
        User user = userService.findById(companyDto.getUserId());

        String logoUrl = fileUploadService.setFile(companyDto.getLogo());
        companyToUpdate.setCompanyLogo(logoUrl);
        companyToUpdate.setCompanyName(companyDto.getName());
        companyToUpdate.setCompanyEmail(companyDto.getEmail());
        companyToUpdate.setUser(user);
        companyToUpdate.setCity(companyDto.getCity());
        companyToUpdate.setState(companyDto.getState());
        companyToUpdate.setZipCode(companyDto.getZipCode());
        companyToUpdate.setCompanyAddress(companyDto.getAddress());
        companyToUpdate.setCompanyPhone(companyDto.getPhone());
        companyToUpdate.setTenant(tenant);

        return companyToUpdate;
    }
}

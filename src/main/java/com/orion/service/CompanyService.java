package com.orion.service;

import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.TenantContext;
import com.orion.dto.category.CategoryDto;
import com.orion.dto.company.CompanyDto;
import com.orion.entity.Company;
import com.orion.entity.Tenant;
import com.orion.entity.User;
import com.orion.repository.CompanyRepository;
import com.orion.security.CustomUserDetails;
import com.orion.service.UserService.UserService;
import com.orion.util.DtoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CompanyService extends BaseService {
    private final CompanyRepository companyRepository;
    private UserService userService;
    private final TenantService tenantService;
    private final FileUploadService fileUploadService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public ResponseObject createCompany(CompanyDto companyDto)  {
        String methodName = "createCompany";
        log.info("{} -> create company", methodName);
        ResponseObject responseObject = new ResponseObject();
        Company company = new Company();

        setCompanyLogo(companyDto,company);
        companyAttributes(companyDto, company);
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        responseObject.setData(company);

        return responseObject;
    }

    public ResponseObject getCompanyById(Long companyId,  String currentUserEmail) {
        String methodName = "getCompanyById";
        log.info("{} -> get company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant= tenantService.findById();

        Optional<CompanyDto> company = companyRepository.findCompany(companyId, currentUserEmail, tenant.getId());
        isPresent(company);

        List<CategoryDto> categories = companyRepository.findCompanyCategories(companyId, tenant.getId());
        if(!categories.isEmpty()){
            company.get().setCategories(categories);
        }

        responseObject.setData(company);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject updateCompany(Long companyId, CompanyDto companyDto) {
        String methodName = "updateCompany";
        log.info("{} -> update company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();

        Company companyToUpdate=findById(companyId);
        setCompanyLogo(companyDto,companyToUpdate);
        companyAttributes(companyDto, companyToUpdate);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        responseObject.setData(companyToUpdate);

        return responseObject;
    }

    private void companyAttributes(CompanyDto companyDto, Company companyToUpdate) {
        companyToUpdate.setCompanyName(companyDto.getName());
        companyToUpdate.setCompanyEmail(companyDto.getEmail());
        Tenant tenant = tenantService.findById();
        User user = userService.findById(companyDto.getUserId());

        companyToUpdate.setUser(user);
        DtoUtils.setIfNotNull(companyDto.getCity(), companyToUpdate::setCity);
        DtoUtils.setIfNotNull(companyDto.getState(), companyToUpdate::setState);
        DtoUtils.setIfNotNull(companyDto.getZipCode(), companyToUpdate::setZipCode);
        DtoUtils.setIfNotNull(companyDto.getAddress(), companyToUpdate::setCompanyAddress);
        DtoUtils.setIfNotNull(companyDto.getPhone(), companyToUpdate::setCompanyPhone);
        companyToUpdate.setTenant(tenant);
        companyToUpdate.setTenant(tenant);

        companyRepository.save(companyToUpdate);
    }

    public ResponseObject deleteCompany(Long companyId) {
        String methodName = "deleteCompany";
        log.info("{} -> delete company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();
        Optional<Company> company = companyRepository.findCompanyById(companyId, tenant.getId());
        isPresent(company);

        Company companyToDelete = company.get();
        companyToDelete.setDeletedAt(LocalDateTime.now());
        companyRepository.save(companyToDelete);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject getMyCompanies(String currentUserEmail) {
        String methodName = "getMyCompanies";
        log.info("{} -> get my companies", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();
        List<CompanyDto> companyList = companyRepository.findAllCompanies(currentUserEmail,tenant.getId());

        for(CompanyDto company : companyList){
            List<CategoryDto> categories = companyRepository.findCompanyCategories(company.getId(), tenant.getId());
            if(!categories.isEmpty()){
                company.setCategories(categories);
            }
        }
        responseObject.setData(companyList);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public List<CompanyDto> findAllCompanies(String email) {
        List<CompanyDto> companyDto = companyRepository.findAllCompanies(email, TenantContext.getCurrentTenant().getId());

        if(companyDto.isEmpty()){
            return Collections.emptyList();
        }

        return companyDto;
    }

    public Company findById(Long id){
        Optional<Company> company = companyRepository.findCompanyById(id, TenantContext.getCurrentTenant().getId());
        isPresent(company);
        return company.get();
    }

    public void setCompanyLogo(CompanyDto companyDto, Company company){
        try {
            if (companyDto.getLogo() != null && !companyDto.getLogo().isEmpty()) {
                String logoUrl = fileUploadService.uploadFile(companyDto.getLogo());
                company.setCompanyLogo(logoUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.category.CategoryDto;
import com.orion.dto.company.CompanyDto;
import com.orion.entity.Company;
import com.orion.entity.Tenant;
import com.orion.entity.User;
import com.orion.repository.CompanyRepository;
import com.orion.repository.TenantRepository;
import com.orion.repository.UserRepository;
import com.orion.util.DtoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CompanyService extends BaseService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    public ResponseObject createCompany(CompanyDto companyDto) {
        String methodName = "createCompany";
        log.info("{} -> create company", methodName);
        ResponseObject responseObject = new ResponseObject();
        Company company = new Company();

        companyAttributes(companyDto, company);
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        responseObject.setData(company);

        return responseObject;
    }

    public ResponseObject getCompanyById(Long companyId,  UserDetails principal) {
        String methodName = "getCompanyById";
        log.info("{} -> get company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<CompanyDto> company = companyRepository.findCompany(companyId, principal.getUsername(), tenant.get().getId());
        isPresent(company);

        List<CategoryDto> categories = companyRepository.findCompanyCategories(companyId, tenant.get().getId());
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

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Company> company = companyRepository.findCompanyById(companyId, tenant.get().getId());
        isPresent(company);

        Company companyToUpdate = company.get();
        companyAttributes(companyDto, companyToUpdate);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        responseObject.setData(companyToUpdate);

        return responseObject;
    }

    private void companyAttributes(CompanyDto companyDto, Company companyToUpdate) {
        companyToUpdate.setCompanyName(companyDto.getCompanyName());
        companyToUpdate.setCompanyEmail(companyDto.getCompanyEmail());
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);
        Optional<User> user = userRepository.findUserIdDeleteAtNull(companyDto.getUserId(), tenant.get().getId());
        isPresent(user);


        companyToUpdate.setUser(user.get());
        DtoUtils.setIfNotNull(companyDto.getCompanyLogo(), companyToUpdate::setCompanyLogo);
        DtoUtils.setIfNotNull(companyDto.getCity(), companyToUpdate::setCity);
        DtoUtils.setIfNotNull(companyDto.getState(), companyToUpdate::setState);
        DtoUtils.setIfNotNull(companyDto.getZipCode(), companyToUpdate::setZipCode);
        DtoUtils.setIfNotNull(companyDto.getCompanyAddress(), companyToUpdate::setCompanyAddress);
        DtoUtils.setIfNotNull(companyDto.getCompanyPhone(), companyToUpdate::setCompanyPhone);
        companyToUpdate.setTenant(tenant.get());

        companyToUpdate.setTenant(tenant.get());

        companyRepository.save(companyToUpdate);
    }

    public ResponseObject deleteCompany(Long companyId) {
        String methodName = "deleteCompany";
        log.info("{} -> delete company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);
        Optional<Company> company = companyRepository.findCompanyById(companyId, tenant.get().getId());
        isPresent(company);

        Company companyToDelete = company.get();
        companyToDelete.setDeletedAt(LocalDateTime.now());
        companyRepository.save(companyToDelete);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject getMyCompanies(UserDetails principal) {
        String methodName = "getMyCompanies";
        log.info("{} -> get my companies", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);
        List<CompanyDto> companyList = companyRepository.findAllCompanies(principal.getUsername(),tenant.get().getId());


        for(CompanyDto company : companyList){
            List<CategoryDto> categories = companyRepository.findCompanyCategories(company.getId(), tenant.get().getId());
            if(!categories.isEmpty()){
                company.setCategories(categories);
            }
        }
        responseObject.setData(companyList);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }


}

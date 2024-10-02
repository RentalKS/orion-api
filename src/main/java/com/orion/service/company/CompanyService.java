package com.orion.service.company;

import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.category.CategoryDto;
import com.orion.dto.company.CompanyDto;
import com.orion.entity.Company;
import com.orion.entity.Tenant;
import com.orion.mapper.CompanyMapper;
import com.orion.repository.CompanyRepository;
import com.orion.service.BaseService;
import com.orion.service.user.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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
    private final TenantService tenantService;
    private final CompanyMapper companyMapper;
    public ResponseObject createCompany(CompanyDto companyDto)  {
        String methodName = "createCompany";
        log.info("{} -> create company", methodName);
        ResponseObject responseObject = new ResponseObject();

        Company company = companyMapper.toEntity(companyDto,new Company());
        companyRepository.save(company);

        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        responseObject.setData(company);

        return responseObject;
    }

    public ResponseObject getCompanyById(Long companyId) {
        String methodName = "getCompanyById";
        log.info("{} -> get company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();

        Optional<CompanyDto> companyDto = companyRepository.findCompany(companyId,ConfigSystem.getTenant().getId());
        isPresent(companyDto);

        List<CategoryDto> categories = companyRepository.findCompanyCategories(companyId, ConfigSystem.getTenant().getId());
        if(!categories.isEmpty()){
            companyDto.get().setCategories(categories);
        }

        responseObject.setData(companyDto);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject updateCompany(Long companyId, CompanyDto companyDto) {
        String methodName = "updateCompany";
        log.info("{} -> update company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();

        Company companyToUpdate=findById(companyId);
        companyToUpdate = companyMapper.toEntity(companyDto,companyToUpdate);
        companyRepository.save(companyToUpdate);

        responseObject.prepareHttpStatus(HttpStatus.OK);
        responseObject.setData(companyToUpdate);

        return responseObject;
    }


    public ResponseObject deleteCompany(Long companyId) {
        String methodName = "deleteCompany";
        log.info("{} -> delete company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();

        Company companyToDelete = findById(companyId);
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
        List<CompanyDto> companyDto = companyRepository.findAllCompanies(email, ConfigSystem.getTenant().getId());

        if(companyDto.isEmpty()){
            return Collections.emptyList();
        }

        return companyDto;
    }

    public Company findById(Long id){
        Optional<Company> company = companyRepository.findCompanyById(id, ConfigSystem.getTenant().getId());
        isPresent(company);
        return company.get();
    }
}

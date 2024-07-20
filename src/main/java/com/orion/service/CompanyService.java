package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.dto.CompanyDto;
import com.orion.entity.Company;
import com.orion.entity.User;
import com.orion.repository.CompanyRepository;
import com.orion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CompanyService extends BaseService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

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
        Optional<CompanyDto> company = companyRepository.findCompany(companyId, principal.getUsername());
        isPresent(company);

        responseObject.setData(company);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject updateCompany(Long companyId, CompanyDto companyDto) {
        String methodName = "updateCompany";
        log.info("{} -> update company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();
        Optional<Company> company = companyRepository.findCompanyById(companyId);
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

        Optional<User> user = userRepository.findUserById(companyDto.getUserId());
        isPresent(user);

        companyToUpdate.setUser(user.get());

        if(companyDto.getCity() != null){
            companyToUpdate.setCity(companyDto.getCity());
        }
        if(companyDto.getState() != null){
            companyToUpdate.setState(companyDto.getState());
        }
        if(companyDto.getZipCode() != null){
            companyToUpdate.setZipCode(companyDto.getZipCode());
        }
        if(companyDto.getCompanyAddress() != null){
            companyToUpdate.setCompanyAddress(companyDto.getCompanyAddress());
        }
        if(companyDto.getCompanyPhone() != null){
            companyToUpdate.setCompanyPhone(companyDto.getCompanyPhone());
        }

        companyRepository.save(companyToUpdate);
    }

    public ResponseObject deleteCompany(Long companyId) {
        String methodName = "deleteCompany";
        log.info("{} -> delete company by id : {}", methodName, companyId);
        ResponseObject responseObject = new ResponseObject();
        Optional<Company> company = companyRepository.findCompanyById(companyId);
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
        responseObject.setData(companyRepository.findAllCompanies(principal.getUsername()));
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }


}
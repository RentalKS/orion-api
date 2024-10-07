package com.orion.service.category;

import com.orion.entity.User;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.category.CategoryDto;
import com.orion.entity.Category;
import com.orion.entity.Company;
import com.orion.entity.Tenant;
import com.orion.repository.CategoryRepository;
import com.orion.service.BaseService;
import com.orion.service.company.CompanyService;
import com.orion.service.user.TenantService;
import com.orion.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryService extends BaseService {
    private final TenantService tenantService;
    private final CategoryRepository categoryRepository;
    private final CompanyService companyService;
    private final UserService userService;

    public ResponseObject createCategory(CategoryDto categoryDto) {
        String methodName = "createCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();
        Company company = companyService.findById(categoryDto.getCompanyId());

        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        if(categoryDto.getCategoryDescription() != null) {
            category.setCategoryDescription(categoryDto.getCategoryDescription());
        }
        category.setCompany(company);
        category.setTenant(tenant);

        responseObject.setData(categoryRepository.save(category));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getCategory(Long categoryId,String currentEmail) {
        String methodName = "getCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<CategoryDto> category = categoryRepository.findCategoryByIdFromDto(categoryId,ConfigSystem.getTenant().getId(),currentEmail);
        isPresent(category);

        responseObject.setData(category.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateCategory(Long categoryId,CategoryDto categoryDto) {
        String methodName = "updateCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Category category = findById(categoryId);

        category.setCategoryName(categoryDto.getCategoryName());
        if (categoryDto.getCategoryDescription() != null) {
            category.setCategoryDescription(categoryDto.getCategoryDescription());
        }
        responseObject.setData(categoryRepository.save(category));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteCategory(Long categoryId) {
        String methodName = "deleteCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Category category = findById(categoryId);

        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAllCategory(String currentEmail) {
        String methodName = "getAllCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        User user = userService.findByEmail(currentEmail);
        List<String> emails = new ArrayList<>(Collections.singletonList(currentEmail));

        if(user.getRole().getName().equals(getRoleTenant())){
           List<String> emailsOfAgencies = userService.findUsersIdsByTenant(user.getId());
           emails.addAll(emailsOfAgencies);
        }

        List<CategoryDto> categoryDto = categoryRepository.findAllCategory(ConfigSystem.getTenant().getId(),emails);

        responseObject.setData(Optional.ofNullable(categoryDto).orElse(Collections.emptyList()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public Category findById(Long id){
        Optional<Category> category = categoryRepository.findCategoryById(id,ConfigSystem.getTenant().getId());
        isPresent(category);
        return category.get();
    }
}

package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.dto.category.CategoryDto;
import com.orion.entity.Category;
import com.orion.entity.Company;
import com.orion.repository.CategoryRepository;
import com.orion.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryService extends BaseService {
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    public ResponseObject createCategory(CategoryDto categoryDto) {
        String methodName = "createCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Company> company = companyRepository.findCompanyById(categoryDto.getCompanyId());
        isPresent(company);

        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        if(categoryDto.getCategoryDescription() != null) {
            category.setCategoryDescription(categoryDto.getCategoryDescription());
        }
        category.setCompany(company.get());

        responseObject.setData(categoryRepository.save(category));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getCategory(Long categoryId) {
        String methodName = "getCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<CategoryDto> category = categoryRepository.findCategoryByIdFromDto(categoryId);
        isPresent(category);

        responseObject.setData(category.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateCategory(CategoryDto categoryDto) {
        String methodName = "updateCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Category> category = categoryRepository.findCategoryById(categoryDto.getId());
        isPresent(category);

        category.get().setCategoryName(categoryDto.getCategoryName());
        if (categoryDto.getCategoryDescription() != null) {
            category.get().setCategoryDescription(categoryDto.getCategoryDescription());
        }
        responseObject.setData(categoryRepository.save(category.get()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteCategory(Long categoryId) {
        String methodName = "deleteCategory";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Category> category = categoryRepository.findCategoryById(categoryId);
        isPresent(category);

        category.get().setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}

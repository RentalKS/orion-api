package com.orion.repository;

import com.orion.dto.category.CategoryDto;
import com.orion.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select new com.orion.dto.category.CategoryDto(c.id, c.createdAt, c.categoryName, c.categoryDescription, c.company.id) " +
            "from Category c where c.id = :categoryId and c.deletedAt is null and c.tenant.id = :tenantId")
    Optional<CategoryDto> findCategoryByIdFromDto(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

    @Query("select c from Category c where c.id = :categoryId and c.deletedAt is null and c.tenant.id = :tenantId")
    Optional<Category> findCategoryById(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

     @Query("select new com.orion.dto.category.CategoryDto(c.id, c.createdAt, c.categoryName, c.categoryDescription, c.company.id) " +
            "from Category c where c.tenant.id = :tenantId and c.deletedAt is null")
     List<CategoryDto> findAllCategory(@Param("tenantId") Long tenantId);
}
package com.orion.repository;

import com.orion.dto.category.CategoryDto;
import com.orion.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select new com.orion.dto.category.CategoryDto(c.id, c.createdAt, c.categoryName, c.categoryDescription, c.company.id) " +
            "from Category c where c.id = :categoryId and c.deletedAt is null")
    Optional<CategoryDto> findCategoryByIdFromDto(Long categoryId);

    @Query("select c from Category c where c.id = :categoryId and c.deletedAt is null")
    Optional<Category> findCategoryById(@Param("categoryId") Long categoryId);
}
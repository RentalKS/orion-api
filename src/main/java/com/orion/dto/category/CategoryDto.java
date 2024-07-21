package com.orion.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.Category}
 */
@Value
public class CategoryDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    @NotNull
    String categoryName;
    String categoryDescription;
    Long companyId;
}
package com.orion.dto.category;

import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.Category}
 */
@Getter
@Setter
public class CategoryDto implements Serializable {
    Long id;
    Long createdAt;
    @NotNull
    String categoryName;
    String categoryDescription;
    Long companyId;

    public CategoryDto(Long id, LocalDateTime createdAt,String categoryName,String categoryDescription,Long companyId){
        this.id=id;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.categoryName=categoryName;
        this.categoryDescription=categoryDescription;
        this.companyId = companyId;
    }
}
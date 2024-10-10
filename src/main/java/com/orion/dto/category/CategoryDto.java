package com.orion.dto.category;

import com.orion.dto.section.SectionDto;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.orion.entity.Category}
 */
@Getter
@Setter
public class CategoryDto implements Serializable {
    private Long id;
    private Long createdAt;
    @NotNull
    private String categoryName;
    private String categoryDescription;
    private Long companyId;
    private List<SectionDto> sectionList;

    public CategoryDto(Long id, LocalDateTime createdAt,String categoryName,String categoryDescription,Long companyId){
        this.id=id;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.categoryName=categoryName;
        this.categoryDescription=categoryDescription;
        this.companyId = companyId;
    }
}
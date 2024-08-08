package com.orion.dto.section;

import com.orion.entity.Section;
import com.orion.util.DateUtil;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Section}
 */
@Value
public class SectionDto implements Serializable {
    Long id;
    Long createdAt;
    String sectionName;
    String sectionDescription;
    String sectionImage;
    Long categoryId;

    public SectionDto(Long id, LocalDateTime createdAt, String sectionName, String sectionDescription, String sectionImage, Long categoryId) {
        this.id = id;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.sectionName = sectionName;
        this.sectionDescription = sectionDescription;
        this.sectionImage = sectionImage;
        this.categoryId = categoryId;
    }
}
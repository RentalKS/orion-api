package com.orion.dto.section;

import com.orion.entity.Section;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Section}
 */
@Value
public class SectionDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    String sectionName;
    String sectionDescription;
    String sectionImage;
    Long categoryId;
}
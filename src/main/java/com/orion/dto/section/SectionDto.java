package com.orion.dto.section;

import com.orion.entity.Section;
import com.orion.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Section}
 */
@Getter
@Setter
public class SectionDto implements Serializable {
    Long id;
    Long createdAt;
    String sectionName;
    String sectionDescription;
    MultipartFile sectionImage;
    String sectionImageUrl;
    Long categoryId;

    public SectionDto(Long id, LocalDateTime createdAt, String sectionName, String sectionDescription, String sectionImageUrl, Long categoryId) {
        this.id = id;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.sectionName = sectionName;
        this.sectionDescription = sectionDescription;
        this.sectionImageUrl = sectionImageUrl;
        this.categoryId = categoryId;
    }
}
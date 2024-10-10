package com.orion.dto.section;

import com.orion.dto.vehicle.VehicleDto;
import com.orion.entity.Section;
import com.orion.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Section}
 */
@Getter
@Setter
public class SectionDto implements Serializable {
    private Long id;
    private Long createdAt;
    private String sectionName;
    private String sectionDescription;
    private MultipartFile sectionImage;
    private String sectionImageUrl;
    private Long categoryId;
    private List<VehicleDto> vehicleList;


    public SectionDto(Long id, LocalDateTime createdAt, String sectionName, String sectionDescription, String sectionImageUrl, Long categoryId) {
        this.id = id;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.sectionName = sectionName;
        this.sectionDescription = sectionDescription;
        this.sectionImageUrl = sectionImageUrl;
        this.categoryId = categoryId;
    }
}
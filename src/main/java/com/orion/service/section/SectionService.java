package com.orion.service.section;

import com.orion.generics.ResponseObject;
import com.orion.entity.Category;
import com.orion.entity.Section;
import com.orion.dto.section.SectionDto;
import com.orion.entity.Tenant;
import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.repository.SectionRepository;
import com.orion.service.BaseService;
import com.orion.service.category.CategoryService;
import com.orion.service.user.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SectionService extends BaseService {
    private final TenantService tenantService;
    private final SectionRepository sectionRepository;
    private final CategoryService categoryService;
    private final FileUploadService fileUploadService;

    public ResponseObject createSection(SectionDto sectionDto) {
        String methodName = "createSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();
        Category category = categoryService.findById(sectionDto.getCategoryId());

        Section section = new Section();
        section.setSectionName(sectionDto.getSectionName());

        if(sectionDto.getSectionDescription() != null) {
            section.setSectionDescription(sectionDto.getSectionDescription());
        }
        String imageUrl = fileUploadService.setFile(sectionDto.getSectionImage());
        section.setSectionImage(imageUrl);

        section.setCategory(category);
        section.setTenant(tenant);

        responseObject.setData(sectionRepository.save(section));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getSection(Long sectionId,String currentEmail) {
        String methodName = "getSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<SectionDto> section = sectionRepository.findSectionById(sectionId, ConfigSystem.getTenant().getId(),currentEmail);
        isPresent(section);

        responseObject.setData(section.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAllSections(String currentEmail) {
        String methodName = "getAllSections";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        responseObject.setData(sectionRepository.findAllSections(ConfigSystem.getTenant().getId(),currentEmail));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public Section findById(Long sectionId) {
        Optional<Section> section = sectionRepository.findById(sectionId);
        isPresent(section);
        return section.get();
    }

    public ResponseObject updateSection(Long sectionId,SectionDto sectionDto) {
        String methodName = "updateSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Section section = findById(sectionId);
        String imageUrl = fileUploadService.setFile(sectionDto.getSectionImage());
        section.setSectionImage(imageUrl);

        if(sectionDto.getSectionName() != null) {
            section.setSectionName(sectionDto.getSectionName());
        }
        if(sectionDto.getSectionDescription() != null) {
            section.setSectionDescription(sectionDto.getSectionDescription());
        }

        responseObject.setData(sectionRepository.save(section));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteSection(Long sectionId) {
        String methodName = "deleteSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Section> section = sectionRepository.findById(sectionId);
        isPresent(section);
        section.get().setDeletedAt(LocalDateTime.now());
        sectionRepository.save(section.get());

        responseObject.prepareHttpStatus(HttpStatus.NO_CONTENT);
        return responseObject;
    }

}

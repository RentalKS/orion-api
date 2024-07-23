package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.entity.Category;
import com.orion.entity.Section;
import com.orion.dto.section.SectionDto;
import com.orion.entity.Tenant;
import com.orion.repository.CategoryRepository;
import com.orion.repository.SectionRepository;
import com.orion.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SectionService extends BaseService{
    private final TenantRepository tenantRepository;
    private final SectionRepository sectionRepository;
    private final CategoryRepository categoryRepository;
    public ResponseObject createSection(SectionDto sectionDto) {
        String methodName = "createSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Category> category = categoryRepository.findCategoryById(sectionDto.getCategoryId(), tenant.get().getId());
        isPresent(category);

        Section section = new Section();
        section.setSectionName(sectionDto.getSectionName());

        if(sectionDto.getSectionDescription() != null) {
            section.setSectionDescription(sectionDto.getSectionDescription());
        }

        section.setCategory(category.get());
        section.setTenant(tenant.get());

        responseObject.setData(sectionRepository.save(section));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getSection(Long sectionId) {
        String methodName = "getSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<SectionDto> section = sectionRepository.findSectionById(sectionId, tenant.get().getId());
        isPresent(section);

        responseObject.setData(section.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateSection(SectionDto sectionDto) {
        String methodName = "updateSection";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Section> section = sectionRepository.findById(sectionDto.getId());
        isPresent(section);

        if(sectionDto.getSectionName() != null) {
            section.get().setSectionName(sectionDto.getSectionName());
        }
        if(sectionDto.getSectionDescription() != null) {
            section.get().setSectionDescription(sectionDto.getSectionDescription());
        }

        responseObject.setData(sectionRepository.save(section.get()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }


}

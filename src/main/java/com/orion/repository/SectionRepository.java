package com.orion.repository;

import com.orion.entity.Section;
import com.orion.dto.section.SectionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("select new com.orion.dto.section.SectionDto(s.id, s.createdAt, s.sectionName, s.sectionDescription, s.sectionImage, s.category.id) " +
            "from Section s where s.id = :sectionId and s.deletedAt is null")
    Optional<SectionDto> findSectionById(Long sectionId);
}
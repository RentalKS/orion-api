package com.orion.repository;

import com.orion.entity.Section;
import com.orion.dto.section.SectionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("select new com.orion.dto.section.SectionDto(s.id, s.createdAt, s.sectionName, s.sectionDescription, s.sectionImage, s.category.id) " +
            "from Section s where s.id = :sectionId and s.deletedAt is null and s.tenant.id = :tenantId and s.createdBy = :email")
    Optional<SectionDto> findSectionById(@Param("sectionId") Long sectionId, @Param("tenantId") Long tenantId,@Param("email") String email);

    @Query("select new com.orion.dto.section.SectionDto(s.id, s.createdAt, s.sectionName, s.sectionDescription, s.sectionImage, s.category.id) " +
            "from Section s where s.deletedAt is null and s.tenant.id = :tenantId and s.createdBy = :email ")
    List<SectionDto> findAllSections(@Param("tenantId") Long tenantId,@Param("email") String email);


    @Query("select new com.orion.dto.section.SectionDto(s.id, s.createdAt, s.sectionName, s.sectionDescription, s.sectionImage, s.category.id) " +
            "from Section s where s.category.id = :id and s.deletedAt is null and s.tenant.id = :id1")
    List<SectionDto> findSectionsFromCategory(Long id, Long id1);
}
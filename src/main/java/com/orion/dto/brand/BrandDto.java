package com.orion.dto.brand;

import com.orion.dto.model.ModelDto;
import com.orion.enums.BrandAccess;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BrandDto {
    private Long id;
    private String logoUrl;
    @NotNull
    private BrandAccess name;
    private String description;
    private MultipartFile logo;
    private List<ModelDto> modelDtoList;

    public BrandDto(Long id, BrandAccess name,String logoUrl,String description){
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
    }
}

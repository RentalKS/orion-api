package com.orion.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ModelDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long brandId;
    @NotBlank
    private String type; // e.g., Sedan, SUV, etc.
    @NotNull
    private Long seatingCapacity;
    @NotBlank
    private String fuelEfficiency; // e.g., MPG
    private String modelImageUrl;
    private MultipartFile modelImage;

    public ModelDto(Long id, String name, Long brandId, String type, Long seatingCapacity, String fuelEfficiency,String modelImageUrl){
        this.id = id;
        this.name =name;
        this.brandId = brandId;
        this.type = type;
        this.seatingCapacity = seatingCapacity;
        this.fuelEfficiency = fuelEfficiency;
        this.modelImageUrl = modelImageUrl;
    }
}

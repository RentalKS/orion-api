package com.orion.dto.model;

import com.orion.dto.vehicle.VehicleDto;
import com.orion.enums.model.ModelAccess;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModelDto {
    private Long id;
    @NotNull
    private ModelAccess name;
    private Long brandId;
    @NotBlank
    private String type; // e.g., Sedan, SUV, etc.
    @NotNull
    private Long seatingCapacity;
    @NotBlank
    private String fuelEfficiency; // e.g., MPG
    private String modelImageUrl;
    private MultipartFile modelImage;
    private List<VehicleDto> vehicles;

    public ModelDto(Long id, ModelAccess name, Long brandId, String type, Long seatingCapacity, String fuelEfficiency,String modelImageUrl){
        this.id = id;
        this.name =name;
        this.brandId = brandId;
        this.type = type;
        this.seatingCapacity = seatingCapacity;
        this.fuelEfficiency = fuelEfficiency;
        this.modelImageUrl = modelImageUrl;
    }
}

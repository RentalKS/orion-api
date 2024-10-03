package com.orion.mapper;

import com.orion.dto.vehicle.VehicleCreateDto;
import com.orion.entity.*;
import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.service.location.LocationService;
import com.orion.service.model.ModelService;
import com.orion.service.rental.RateDatesService;
import com.orion.service.user.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VehicleMapper {
    private final ModelService modelService;
    private final LocationService locationService;
    private final RateDatesService rateDateService;
    private final TenantService tenantService;
    private final FileUploadService fileUploadService;
    public Vehicle toEntity(VehicleCreateDto vehicleDto, Vehicle vehicle) {
        Model model = modelService.findModelById(vehicleDto.getModelId());
        Location location = locationService.findLocationById(vehicleDto.getLocationId());
        RateDates rateDates = rateDateService.findById(vehicleDto.getRateId());
        Tenant tenant = tenantService.findById();

        vehicle.setRateDates(rateDates);
        vehicle.setLocation(location);
        vehicle.setModel(model);
        vehicle.setTenant(tenant);

        vehicle.setRegistrationNumber(vehicleDto.getRegistrationNumber());
        vehicle.setYear(vehicleDto.getYear());
        vehicle.setFuelType(vehicleDto.getFuelType());
        vehicle.setMileage(vehicleDto.getMileage());
        vehicle.setTransmission(vehicleDto.getTransmission());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setDescription(vehicleDto.getDescription());
        String imageUrl = fileUploadService.setFile(vehicleDto.getImage());
        vehicle.setImage(imageUrl);
        return vehicle;
    }
}

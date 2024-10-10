package com.orion.service.brand;
import com.orion.dto.brand.BrandDto;
import com.orion.dto.model.ModelDto;
import com.orion.entity.Brand;
import com.orion.entity.Tenant;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.repository.BrandRepository;
import com.orion.service.BaseService;
import com.orion.service.model.ModelService;
import com.orion.service.user.TenantService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class BrandService extends BaseService {
    private final TenantService tenantService;
    private final BrandRepository brandRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    @Lazy
    private ModelService modelService;

    public BrandService(TenantService tenantService, BrandRepository brandRepository, FileUploadService fileUploadService) {
        this.tenantService = tenantService;
        this.brandRepository = brandRepository;
        this.fileUploadService = fileUploadService;
    }

    public Brand findById(Long id){
        Optional<Brand> brand = brandRepository.findById(id);
        isPresent(brand);
        return brand.get();
    }
    public ResponseObject create(BrandDto brandDto) {
        String methodName = "create";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();

        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brand.setDescription(brandDto.getDescription());
        String logoUrl = fileUploadService.setFile(brandDto.getLogo());
        brand.setLogo(logoUrl);
        brand.setTenant(tenant);

        brandRepository.save(brand);
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        responseObject.setData(brand.getId());

        return responseObject;
    }

    public ResponseObject get(Long locationId,String currentEmail) {
        String methodName = "getLocation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<BrandDto> brand = brandRepository.findBrandByIdAndByTenant(locationId, ConfigSystem.getTenant().getId());
        isPresent(brand);
        List<ModelDto> modelDtoList = modelService.findModelsFromBrand(brand.get().getId(),currentEmail);
        brand.get().setModelDtoList(modelDtoList);

        responseObject.setData(brand.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAll(String currentEmail) {
        String methodName = "getAllLocations";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Long tenantId = ConfigSystem.getTenant().getId();

        List<BrandDto> brandDtoList = brandRepository.findAllBrandsByTenant(tenantId,currentEmail);
        for(BrandDto brandDto :  brandDtoList){
            List<ModelDto> modelDtoList = modelService.findModelsFromBrand(brandDto.getId(),currentEmail);
            brandDto.setModelDtoList(modelDtoList);
        }
        responseObject.setData(Optional.of(brandDtoList).orElseGet(Collections::emptyList));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject update(Long locationId,BrandDto brandDto) {
        String methodName = "updateBrand";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Brand brandToUpdate = findById(locationId);
        brandToUpdate.setName(brandDto.getName());
        brandToUpdate.setDescription(brandDto.getDescription());
        String logoUrl = fileUploadService.setFile(brandDto.getLogo());
        brandToUpdate.setLogo(logoUrl);
        brandRepository.save(brandToUpdate);

        responseObject.prepareHttpStatus(HttpStatus.OK);
        responseObject.setData(brandToUpdate.getId());
        return responseObject;
    }
    public ResponseObject delete(Long locationId) {
        String methodName = "deleteBrand";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Brand brandToDelete = findById(locationId);
        brandToDelete.setDeletedAt(LocalDateTime.now());

        brandRepository.save(brandToDelete);
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}
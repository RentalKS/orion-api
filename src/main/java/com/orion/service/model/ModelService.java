package com.orion.service.model;

import com.orion.entity.Brand;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.model.ModelDto;
import com.orion.entity.Model;
import com.orion.entity.Tenant;
import com.orion.repository.ModelRepository;
import com.orion.repository.TenantRepository;
import com.orion.service.BaseService;
import com.orion.service.brand.BrandService;
import com.orion.service.user.TenantService;
import com.orion.util.DtoUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ModelService extends BaseService {
    private final ModelRepository modelRepository;
    private final TenantService tenantService;
    private final BrandService brandService;
    private final FileUploadService fileUploadService;

    public Model findModelById(Long modelId){
         Optional<Model> model = modelRepository.findModelById(modelId,ConfigSystem.getTenant().getId());
        isPresent(model);
        return model.get();
    }
    public ResponseObject createModel(@Valid ModelDto modelDto) {
        String methodName = "createModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        
        Tenant tenant = tenantService.findById();
        Brand brand = brandService.findById(modelDto.getBrandId());

        Model model = new Model();
        model.setName(modelDto.getName());
        model.setBrand(brand);
        model.setType(modelDto.getType());
        model.setSeatingCapacity(modelDto.getSeatingCapacity());
        model.setFuelEfficiency(modelDto.getFuelEfficiency());
        setModelImage(modelDto.getModelImage(),model);
        model.setTenant(tenant);

        responseObject.setData(modelRepository.save(model));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }
    public void setModelImage(MultipartFile modelImage, Model model){
        try {
            if(modelImage == null){
                return;
            }
            String modelUrl =  fileUploadService.uploadFile(modelImage);
            model.setModelImage(modelUrl);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public ResponseObject getModel(Long modelId) {
        String methodName = "getModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();

        Optional<ModelDto> model = modelRepository.findModelByIdFromDto(modelId, tenant.getId());
        isPresent(model);

        responseObject.setData(model.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateModel(Long modelId,ModelDto modelDto) {
        String methodName = "updateModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Model modelToUpdate = findModelById(modelId);
        Brand brand = brandService.findById(modelDto.getBrandId());

        modelToUpdate.setBrand(brand);
        modelToUpdate.setName(modelDto.getName());
        modelToUpdate.setType(modelDto.getType());
        modelToUpdate.setSeatingCapacity(modelDto.getSeatingCapacity());
        modelToUpdate.setFuelEfficiency(modelDto.getFuelEfficiency());
        setModelImage(modelDto.getModelImage(),modelToUpdate);

        responseObject.setData(modelRepository.save(modelToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteModel(Long modelId) {
        String methodName = "deleteModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Model model = findModelById(modelId);

        model.setDeletedAt(LocalDateTime.now());
        modelRepository.save(model);
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAllModels(String currentEmail) {
        String methodName = "getAllModels";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();

        List<ModelDto> models = modelRepository.findAllModelsByTenantId(tenant.getId(),currentEmail);
        responseObject.setData(models);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }
    public List<ModelDto> findModelsFromBrand(Long brandId,String currentEmail){
        List<ModelDto> modelDtoList = modelRepository.findModelsFromThisBrand(brandId,ConfigSystem.getTenant().getId(),currentEmail);
        return Optional.ofNullable(modelDtoList).orElse(Collections.emptyList());
    }
}

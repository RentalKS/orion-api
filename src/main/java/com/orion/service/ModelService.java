package com.orion.service;

import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.TenantContext;
import com.orion.dto.model.ModelDto;
import com.orion.entity.Model;
import com.orion.entity.Tenant;
import com.orion.repository.ModelRepository;
import com.orion.repository.TenantRepository;
import com.orion.util.DtoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ModelService extends BaseService {

    private final ModelRepository modelRepository;
    private final TenantRepository tenantRepository;

    public Model findModelById(Long modelId){
         Optional<Model> model = modelRepository.findModelById(modelId,TenantContext.getCurrentTenant().getId());
        isPresent(model);
        return model.get();
    }
    public ResponseObject createModel(ModelDto modelDto) {
        String methodName = "createModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Model model = new Model();
        model.setName(modelDto.getName());
        model.setBrand(modelDto.getBrand());
        model.setType(modelDto.getType());
        model.setSeatingCapacity(modelDto.getSeatingCapacity());
        model.setFuelEfficiency(modelDto.getFuelEfficiency());
        model.setTenant(tenant.get());

        responseObject.setData(modelRepository.save(model));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getModel(Long modelId) {
        String methodName = "getModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<ModelDto> model = modelRepository.findModelByIdFromDto(modelId, tenant.get().getId());
        isPresent(model);

        responseObject.setData(model.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateModel(Long modelId,ModelDto modelDto) {
        String methodName = "updateModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Model> model = modelRepository.findById(modelId);
        isPresent(model);

        Model modelToUpdate = model.get();
        DtoUtils.setIfNotNull(modelDto.getName(), modelToUpdate::setName);
        DtoUtils.setIfNotNull(modelDto.getBrand(), modelToUpdate::setBrand);
        DtoUtils.setIfNotNull(modelDto.getType(), modelToUpdate::setType);
        DtoUtils.setIfNotNull(modelDto.getSeatingCapacity(), modelToUpdate::setSeatingCapacity);
        DtoUtils.setIfNotNull(modelDto.getFuelEfficiency(), modelToUpdate::setFuelEfficiency);

        responseObject.setData(modelRepository.save(modelToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteModel(Long modelId) {
        String methodName = "deleteModel";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Model> model = modelRepository.findById(modelId);
        isPresent(model);

        model.get().setDeletedAt(LocalDateTime.now());
        modelRepository.save(model.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAllModels() {
        String methodName = "getAllModels";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        List<ModelDto> models = modelRepository.findAllModelsByTenantId(tenant.get().getId());
        responseObject.setData(models);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }
}

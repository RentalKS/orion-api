package com.orion.service.policyVehicle;

import com.orion.dto.vehicle.VehicleCreateDto;
import com.orion.entity.Vehicle;
import com.orion.generics.ResponseObject;
import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.entity.InsurancePolicy;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.mapper.InsurancePolicyMapper;
import com.orion.repository.InsurancePolicyRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class InsurancePolicyService extends BaseService {
    private final InsurancePolicyRepository repository;
    private final InsurancePolicyMapper mapper;

    public ResponseObject create(InsurancePolicyDto insurancePolicyDto) {
        String methodName = "createInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        InsurancePolicy insurancePolicy = mapper.toEntity(insurancePolicyDto, new InsurancePolicy());

        responseObject.setData(this.save(insurancePolicy));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }
    public void createFromVehicle(Vehicle vehicle, InsurancePolicyDto insurancePolicyDto) {
        InsurancePolicy insurancePolicy = mapper.toEntity(insurancePolicyDto, new InsurancePolicy());
        insurancePolicy.setVehicle(vehicle);
        this.save(insurancePolicy);
    }
    public InsurancePolicy findById(Long id) {
        Optional<InsurancePolicy> insurancePolicy = repository.findById(id, ConfigSystem.getTenant().getId());
        isPresent(insurancePolicy);
        return insurancePolicy.get();
    }
    public InsurancePolicy save(InsurancePolicy insurancePolicy) {
        try {
            insurancePolicy = repository.save(insurancePolicy);
        } catch (Exception e) {
            log.error("Error saving insurance policy: {}", e.getMessage());
            throw e;
        }
        return insurancePolicy;
    }


    public ResponseObject get(Long insurancePolicyId) {
        String methodName = "getInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<InsurancePolicyDto> insurancePolicy = repository.findDtoById(insurancePolicyId, ConfigSystem.getTenant().getId());
        isPresent(insurancePolicy);

        responseObject.setData(insurancePolicy.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getAll(String currentEmail) {
        String methodName = "getAllInsurancePolicies";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        List<InsurancePolicyDto> insurancePolicyDtoList = repository.findAllByTenant(ConfigSystem.getTenant().getId(), currentEmail);
        responseObject.setData(Optional.of(insurancePolicyDtoList).orElse(Collections.emptyList()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateInsurancePolicy(Long updateId,InsurancePolicyDto insurancePolicyDto) {
        String methodName = "updateInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        InsurancePolicy insurancePolicyToUpdate = findById(updateId);
        insurancePolicyToUpdate = mapper.toEntity(insurancePolicyDto, insurancePolicyToUpdate);

        responseObject.setData(this.save(insurancePolicyToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject delete(Long insurancePolicyId) {
        String methodName = "deleteInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        InsurancePolicy insurancePolicy = findById(insurancePolicyId);
        insurancePolicy.setDeletedAt(LocalDateTime.now());
        this.save(insurancePolicy);
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}

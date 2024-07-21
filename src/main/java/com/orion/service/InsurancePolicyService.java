package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.entity.InsurancePolicy;
import com.orion.entity.Vehicle;
import com.orion.repository.InsurancePolicyRepository;
import com.orion.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class InsurancePolicyService extends BaseService {
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final VehicleRepository vehicleRepository;

    public ResponseObject createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
        String methodName = "createInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(insurancePolicyDto.getVehicleId());
        isPresent(vehicle);

        InsurancePolicy insurancePolicy = new InsurancePolicy();
        insurancePolicy.setPolicyNumber(insurancePolicyDto.getPolicyNumber());
        insurancePolicy.setProviderName(insurancePolicyDto.getProviderName());
        insurancePolicy.setStartDate(insurancePolicyDto.getStartDate());
        insurancePolicy.setEndDate(insurancePolicyDto.getEndDate());
        insurancePolicy.setCoverageDetails(insurancePolicyDto.getCoverageDetails());
        insurancePolicy.setVehicle(vehicle.get());

        responseObject.setData(insurancePolicyRepository.save(insurancePolicy));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getInsurancePolicy(Long insurancePolicyId) {
        String methodName = "getInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<InsurancePolicy> insurancePolicy = insurancePolicyRepository.findById(insurancePolicyId);
        isPresent(insurancePolicy);

        responseObject.setData(insurancePolicy.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
        String methodName = "updateInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<InsurancePolicy> insurancePolicy = insurancePolicyRepository.findById(insurancePolicyDto.getId());
        isPresent(insurancePolicy);

        InsurancePolicy insurancePolicyToUpdate = insurancePolicy.get();
        insurancePolicyToUpdate.setPolicyNumber(insurancePolicyDto.getPolicyNumber());
        insurancePolicyToUpdate.setProviderName(insurancePolicyDto.getProviderName());
        insurancePolicyToUpdate.setStartDate(insurancePolicyDto.getStartDate());
        insurancePolicyToUpdate.setEndDate(insurancePolicyDto.getEndDate());
        insurancePolicyToUpdate.setCoverageDetails(insurancePolicyDto.getCoverageDetails());

        responseObject.setData(insurancePolicyRepository.save(insurancePolicyToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteInsurancePolicy(Long insurancePolicyId) {
        String methodName = "deleteInsurancePolicy";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<InsurancePolicy> insurancePolicy = insurancePolicyRepository.findById(insurancePolicyId);
        isPresent(insurancePolicy);

        insurancePolicyRepository.delete(insurancePolicy.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}

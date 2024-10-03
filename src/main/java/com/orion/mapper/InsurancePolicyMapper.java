package com.orion.mapper;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.entity.InsurancePolicy;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.service.user.TenantService;
import com.orion.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsurancePolicyMapper {
    private final VehicleService vehicleService;
    private final TenantService tenantService;

    public InsurancePolicy toEntity(InsurancePolicyDto insurancePolicyDto, InsurancePolicy insurancePolicy) {
        Vehicle vehicle = vehicleService.findById(insurancePolicyDto.getVehicleId());
        Tenant tenant = tenantService.findById();
        insurancePolicy.setPolicyNumber(insurancePolicyDto.getPolicyNumber());
        insurancePolicy.setProviderName(insurancePolicyDto.getProviderName());
        insurancePolicy.setStartDate(insurancePolicyDto.getStartDate());
        insurancePolicy.setEndDate(insurancePolicyDto.getEndDate());
        insurancePolicy.setCoverageDetails(insurancePolicyDto.getCoverageDetails());
        insurancePolicy.setVehicle(vehicle);
        insurancePolicy.setTenant(tenant);
        return insurancePolicy;
    }
}

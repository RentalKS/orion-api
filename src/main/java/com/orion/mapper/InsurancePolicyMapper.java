package com.orion.mapper;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.entity.InsurancePolicy;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.service.user.TenantService;
import com.orion.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InsurancePolicyMapper {

    @Autowired
    @Lazy
    private VehicleService vehicleService;

    @Autowired
    @Lazy
    private TenantService tenantService;

    public InsurancePolicy toEntity(Vehicle vehicle,InsurancePolicyDto insurancePolicyDto, InsurancePolicy insurancePolicy) {
        Tenant tenant = tenantService.findById();
        insurancePolicy.setPolicyNumber(insurancePolicyDto.getPolicyNumber());
        insurancePolicy.setProviderName(insurancePolicyDto.getProviderName());
        insurancePolicy.setCoverageDetails(insurancePolicyDto.getCoverageDetails());
        insurancePolicy.setVehicle(vehicle);
        insurancePolicy.setTenant(tenant);
        return insurancePolicy;
    }
}

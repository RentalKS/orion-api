package com.orion.service.UserService;

import com.orion.entity.Tenant;
import com.orion.repository.TenantRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantService extends BaseService {
    private final TenantRepository tenantRepository;

    public Tenant findTenantById(Long tenantId){
        Optional<Tenant> tenant = tenantRepository.findTenantById(tenantId);
        isPresent(tenant);
        return tenant.get();
    }
}

package com.orion.config.tenant.aspect;

import com.orion.config.tenant.TenantContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TenantListener {

    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(Object entity) {
        final String tenantId = TenantContext.getTenantId();
        log.info("Setting tenantId in TenantListener: {}", tenantId);
        ((TenantAware) entity).setTenantId(tenantId);
    }
}

package com.orion.config.tenant;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Log4j2
@Component
public class TenantInterceptor implements WebRequestInterceptor {

    private final String defaultTenant;

    private String tenantId;


    @Autowired
    public TenantInterceptor(@Value("${multitenancy.tenant.default-tenant:#{null}}") String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Override
    public void preHandle(WebRequest request) {
        // todo define tenant based on jwt token key
        if (request.getHeader("X-TENANT-ID") != null) {
            tenantId = request.getHeader("X-TENANT-ID");
        } else if (this.defaultTenant != null) {
            tenantId = this.defaultTenant;
        } else {
            tenantId = ((ServletWebRequest) request).getRequest().getServerName().split("\\.")[0];
        }
        TenantContext.setTenantId(tenantId);
        log.info("Tenant ID in preHandle: {}", TenantContext.getTenantId());

    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {
        // NOOP
    }

}

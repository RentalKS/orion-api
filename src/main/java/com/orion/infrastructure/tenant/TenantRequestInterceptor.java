package com.orion.infrastructure.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Log4j2
@Service
public class TenantRequestInterceptor implements HandlerInterceptor {

    @Autowired
    TenantContextService tenantContextService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) {
        boolean preHandle = true;

        if(ConfigSystem.getTenant() == null)
            tenantContextService.loadTenantContext(request);

        return preHandle;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        ConfigSystem.clear();
    }

}
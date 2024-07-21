package com.orion.config.tenant;

import org.springframework.security.core.userdetails.UserDetails;

public class TenantContext {
    private static ThreadLocal<TenantData> currentTenant = new InheritableThreadLocal<>();
    private static ThreadLocal<UserDetails> applicationUser = new InheritableThreadLocal<>();

    public static TenantData getCurrentTenant() {
        return currentTenant.get();
    }

    public static UserDetails getApplicationUser(){
        return applicationUser.get();
    }

    public static void setCurrentTenant(TenantData tenantData) {
        currentTenant.set(tenantData);
    }

    public static void setApplicationUser(UserDetails authenticationUser){
        applicationUser.set(authenticationUser);
    }

    public static void clear() {
        currentTenant.set(null);
        applicationUser.set(null);
    }
}
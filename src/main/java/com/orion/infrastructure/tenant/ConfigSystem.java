package com.orion.infrastructure.tenant;

import org.springframework.security.core.userdetails.UserDetails;

public class ConfigSystem {
    private static ThreadLocal<Request> currentTenant = new InheritableThreadLocal<>();
    private static ThreadLocal<UserDetails> applicationUser = new InheritableThreadLocal<>();

    public static Request getTenant() {
        return currentTenant.get();
    }
    public static UserDetails getApplicationUser(){
        return applicationUser.get();
    }

    public static void setCurrentTenant(Request tenantData) {
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
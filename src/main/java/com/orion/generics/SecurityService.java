package com.orion.generics;

import com.orion.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    @Value("${role.admin}")
    private String roleAdmin;

    @Value("${role.tenant}")
    private String roleTenant;

    @Value("${role.employee}")
    private String roleEmployee;

    @Value("${role.client}")
    private String roleClient;

    @Value("${role.agency}")
    private String roleAgency;


    public String getRoleAdmin(){
        return this.roleAdmin;
    }

    public String getRoleTenant(){
        return this.roleTenant;
    }

    public String getRoleClient(){
        return this.roleClient;
    }

    public String getRoleAgency() {
        return roleAgency;
    }

    public String getRoleEmployee() {
        return roleEmployee;
    }
    protected boolean isTenant(User user) {
        return user.getRole().getName().equals(getRoleTenant());
    }

    protected boolean isAgency(User user) {
        return user.getRole().getName().equals(getRoleAgency());
    }

}

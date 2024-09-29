package com.orion.service;

import com.orion.infrastructure.tenant.TenantContext;
import com.orion.generics.ResponseObject;
import com.orion.dto.user.TenantDto;
import com.orion.dto.tenant.TenantDataDto;
import com.orion.dto.tenant.UpdateTenantDto;
import com.orion.entity.Role;
import com.orion.entity.Tenant;
import com.orion.entity.User;
import com.orion.exception.ErrorCode;
import com.orion.exception.InternalException;
import com.orion.exception.ThrowException;
import com.orion.repository.RoleRepository;
import com.orion.repository.TenantRepository;
import com.orion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TenantService extends BaseService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;

    @Value("${role.tenant}")
    String roleTenant;

    @Transactional
    public ResponseObject create(TenantDto createTenant) {
        String methodName = "createTenant";

        log.info("{} -> Create new Tenant", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Role> optionalRole = roleRepository.findByName(roleTenant);
        Role role = optionalRole.get();

        User user = new User();
        String password = generateRandomPassword(8);

        Optional<Tenant> optionalTenant = tenantRepository.findByDomain(createTenant.getDomain());
        Optional<User> checkWithEmail = userRepository.findByEmail(createTenant.getEmail());
        if (optionalTenant.isPresent())
            ThrowException.throwConflictException(ErrorCode.DOMAIN_EXIST, "domain");

        if (checkWithEmail.isPresent()) {
            Optional<User> optionalUserTenant = userRepository.findByUserIdAndTenantIdAndDeletedAtIsNull(Long.valueOf(checkWithEmail.get().getId()), optionalTenant.get().getId());
            if (optionalUserTenant.isPresent()) {
                ThrowException.throwConflictException(ErrorCode.EXISTED_EMAIL, "email");
            }
            user = checkWithEmail.get();

        } else {
            user.setEmail(createTenant.getEmail());
            user.setPassword(encryptPassword(password));
            user.setRole(role);
            userRepository.save(user);
        }

        try {

            Tenant tenant = new Tenant();
            tenant.setName(createTenant.getName());
            tenant.setDomain(createTenant.getDomain());
            tenant.setCreatedAt(LocalDateTime.now());

            tenantRepository.save(tenant);

            User userTenant = new User();
            userTenant.setFirstname(createTenant.getFirstName());
            userTenant.setLastname(createTenant.getLastName());
            userTenant.setEmail(createTenant.getEmail());
            userTenant.setPassword(encryptPassword(password));
            userTenant.setRole(role);
            userTenant.setTenant(tenant);

            userRepository.save(userTenant);

            responseObject.setData(tenant.getId());
            responseObject.prepareHttpStatus(HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("{} -> Create new Tenant", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }

        log.info("{} -> Create new Tenant, response status: {}", methodName, responseObject.getCode());
        return responseObject;
    }

    public ResponseObject updateTenant(UpdateTenantDto updateTenant) {

        String methodName = "updateTenant";

        log.info("{} -> Update Tenant By Id", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> optionalTenant = tenantRepository.findById(updateTenant.getId());
        isPresent(optionalTenant);

        Optional<Tenant> domainTenant = tenantRepository.findByDomainAndDeletedAtAndId(updateTenant.getDomain(), updateTenant.getId());
        if (domainTenant.isPresent())
            ThrowException.throwConflictException(ErrorCode.DOMAIN_EXIST, "domain");

        try {
            Tenant tenant = optionalTenant.get();
            tenant.setName(updateTenant.getName());
            tenant.setDomain(updateTenant.getDomain());

            tenantRepository.save(tenant);

            responseObject.setData(tenant.getId());
            responseObject.prepareHttpStatus(HttpStatus.OK);

        } catch (Exception e) {
            log.error("{} -> Update Tenant By Id", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }

        log.info("{} -> Update Tenant By Id, response status: {}", methodName, responseObject.getCode());
        return responseObject;
    }
    public ResponseObject getTenantById(Long tenantId) {

        String methodName = "getTenant";

        log.info("{} -> Get Tenant by Id", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
        isPresent(optionalTenant);

        try {

            Tenant tenant = optionalTenant.get();

            TenantDataDto tenantData = new TenantDataDto();
            tenantData.setId(tenant.getId());
            tenantData.setName(tenant.getName());
            tenantData.setDomain(tenant.getDomain());

            responseObject.prepareHttpStatus(HttpStatus.OK);
            responseObject.setData(tenantData);

        } catch (Exception e) {
            log.error("{} -> Get Tenant by Id", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }

        log.info("{} -> Get Tenant by Id, response status: {}", methodName, responseObject.getCode());
        return responseObject;
    }
    public ResponseObject getAll(Integer page, Integer size, String search) {

        String methodName = "getAllTenants";

        log.info("{} -> Get all Tenants", methodName);
        ResponseObject responseObject = new ResponseObject();

        try {

            Page<TenantDataDto> tenantList = null;

            Pageable sortedById =
                    PageRequest.of(page - 1, size, Sort.by("id").descending());

            if (search != null && search.length() > 0) {
                tenantList = tenantRepository.findAllTenantsWithSearch(search, sortedById);
            }else {
                tenantList = tenantRepository.findAllTenants(sortedById);
            }

            responseObject.setData(mapPage(tenantList));
            responseObject.prepareHttpStatus(HttpStatus.OK);

        } catch (Exception e) {
            log.error("{} -> Get all Tenants", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }

        log.info("{} -> Get all Tenants, response status: {}", methodName, responseObject.getCode());
        return responseObject;
    }

    public ResponseObject deleteTenant(Long tenantId) {

        String methodName = "deleteTenant";

        log.info("{} -> Delete Tenant by Id", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
        isPresent(optionalTenant);

        try {

            Tenant tenant = optionalTenant.get();
            tenant.setDeletedAt(LocalDateTime.now());

            tenantRepository.save(tenant);

            responseObject.prepareHttpStatus(HttpStatus.OK);
            responseObject.setData(tenantId);

        } catch (Exception e) {
            log.error("{} -> Delete Tenant by Id", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }

        log.info("{} -> Delete Tenant by Id, response status: {}", methodName, responseObject.getCode());
        return responseObject;
    }
    public Tenant findById(){
        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);
        return tenant.get();
    }

}

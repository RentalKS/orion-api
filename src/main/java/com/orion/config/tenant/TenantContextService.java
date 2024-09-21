package com.orion.config.tenant;


import com.orion.entity.Tenant;
import com.orion.entity.User;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.repository.TenantRepository;
import com.orion.repository.UserRepository;
import com.orion.security.config.JwtService;
import com.orion.service.BaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class TenantContextService extends BaseService {

    @Value("${default.tenant.domain}")
    private String defaultTenant;

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final JwtService jwtTokenComponent;


    @Transactional
    public void loadTenantContext(HttpServletRequest httpServletRequest) {
        String domain = httpServletRequest.getHeader("Host");

        Tenant tenant = null;
        User user = null;
        List<Tenant> tenants;

        if(domain!=null){
            if(domain.contains("//"))
                domain = domain.split("//")[1];
          
            if(domain.equals("192.168.1.3:8080")){
                domain="localhost:8080";
            }

            Optional<Tenant> optionalTenant = tenantRepository.findByDomain(domain);
            if (!optionalTenant.isPresent()) {
                ThrowException.throwBadRequestApiException(ErrorCode.DOMAIN_NAME_NOT_EXIST, Arrays.asList("domain"));
            }
            tenant = optionalTenant.get();
        }

        if(httpServletRequest.getHeader("Authorization")!=null) {
            String username = jwtTokenComponent.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
            Optional<User>  optionalAuthenticateUser = userRepository.findByEmail(username);

            if(optionalAuthenticateUser.isPresent()){
                user = optionalAuthenticateUser.get();

                tenants = tenantRepository.findByUserId(user.getId());

                if(domain==null){
                    tenant = tenants
                            .stream().min(Comparator.comparing(Tenant::getId)).get();
                }
            }else {
                ThrowException.throwBadRequestApiException(ErrorCode.DOMAIN_NAME_NOT_EXIST, Arrays.asList("domain"));
            }

        } else if(!httpServletRequest.getRequestURI().contains("/auth")){
            Map<String, String[]> params = httpServletRequest.getParameterMap();
            Long tenantId = null;

            if (!params.isEmpty() && params.containsKey("tenantId")) {
                if (StringUtils.isNumeric(params.get("tenantId")[0]) && !params.get("tenantId")[0].isEmpty())
                    tenantId = Long.parseLong(params.get("tenantId")[0]);
            }

            if(tenantId != null) {
                Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
                tenant = optionalTenant.orElseGet(() -> tenantRepository.findByDomain(defaultTenant).get());
            } else
                tenant = tenantRepository.findByDomain(defaultTenant).get();

            TenantContext.setCurrentTenant(new TenantData(tenant.getId(), tenant.getDomain()));
        }

//        if(user!=null) {
//            Optional<User> optionalUserTenant = userTenantRepository.findByTenantIdUserIdAndActivated(tenant.getId(), user.getId(), Status.ACTIVE);
//            if (optionalUserTenant.isEmpty()) {
//                ThrowException.throwBadRequestApiException(ErrorCode.USER_IS_NOT_ACTIVATED, null);
//            }
//        }

        TenantContext.setCurrentTenant(new TenantData(tenant.getId(), tenant.getDomain()));
    }
}

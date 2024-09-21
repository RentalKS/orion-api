package com.orion.service.UserService;

import com.orion.generics.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.company.CompanyDto;
import com.orion.dto.user.UserData;
import com.orion.entity.Role;
import com.orion.entity.Tenant;
import com.orion.entity.User;
import com.orion.repository.CompanyRepository;
import com.orion.repository.TenantRepository;
import com.orion.repository.UserRepository;
import com.orion.dto.user.ChangePasswordRequest;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final CompanyRepository companyRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public ResponseObject myProfile(UserDetails connectedUser) {
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);
        Optional<UserData> userData = repository.findUserDataById(connectedUser.getUsername(), tenant.get().getId());
        isPresent(userData);

        if(Role.AGENCY.getName().equals(userData.get().getRole())) {
            List<CompanyDto> companyDto = companyRepository.findAllCompanies(userData.get().getEmail(), tenant.get().getId());
            userData.get().setCompanies(companyDto);
        }
        responseObject.setData(userData);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }
    public User findByEmail(String email){
        Optional<User> user = repository.findByEmail(email);
        isPresent(user);
        return user.get();
    }

    public List<Long> getAgencyMembers(Long agencyId) {
        return userRepository.findAllIdsByAgency(agencyId);
    }

}
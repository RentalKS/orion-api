package com.orion.service;

import com.orion.infrastructure.tenant.TenantContext;
import com.orion.dto.company.CompanyDto;
import com.orion.generics.ResponseObject;
import com.orion.dto.user.UserData;
import com.orion.entity.Role;
import com.orion.entity.User;
import com.orion.repository.UserRepository;
import com.orion.dto.user.ChangePasswordRequest;
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
    private final CompanyService companyService;
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
        UserData userData = findByEmail(connectedUser.getUsername());

        if(Role.AGENCY.getName().equals(userData.getRole())) {
            List<CompanyDto> companyList= companyService.findAllCompanies(userData.getEmail());
            userData.setCompanies(companyList);
        }
        responseObject.setData(userData);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public UserData findByEmail(String email){
        Optional<UserData> userData = repository.findUserDataById(email, TenantContext.getCurrentTenant().getId());
        isPresent(userData);
        return userData.get();
    }
    public User findById(Long userId){
        Optional<User> user = repository.findUserIdDeleteAtNull(userId, TenantContext.getCurrentTenant().getId());
        isPresent(user);
        return user.get();
    }
}
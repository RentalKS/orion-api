package com.orion.service.user;

import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.company.CompanyDto;
import com.orion.generics.ResponseObject;
import com.orion.dto.user.UserData;
import com.orion.entity.Role;
import com.orion.entity.User;
import com.orion.repository.CompanyRepository;
import com.orion.repository.UserRepository;
import com.orion.dto.user.ChangePasswordRequest;
import com.orion.service.BaseService;
import com.orion.service.customer.CustomerService;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final CompanyRepository  companyRepository;
    private final CustomerService customerService;
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

    public ResponseObject myProfile(String emailCurrentUser) {
        ResponseObject responseObject = new ResponseObject();
        UserData userData = findUserDataByEmail(emailCurrentUser);

        if(Role.AGENCY.getName().equals(userData.getRole())) {
            List<CompanyDto> companyList = companyRepository.findAllCompanies(emailCurrentUser, ConfigSystem.getTenant().getId());
            userData.setCompanies(companyList);
        }
        responseObject.setData(userData);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject myMembers(){
        ResponseObject responseObject = new ResponseObject();
        try {
            List<UserData> users = repository.findUsersIdsByTenant(ConfigSystem.getTenant().getId());
            responseObject.setData(Optional.ofNullable(users).orElse(List.of()));
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
            throw new InternalException(e.getLocalizedMessage());
        }
        return responseObject;
    }

    public UserData findUserDataByEmail(String email){
        Optional<UserData> userData = repository.findUserDataById(email);
        isPresent(userData);
        return userData.get();
    }

    public User findByEmail(String email){
        Optional<User> user = repository.findByEmail(email);
        isPresent(user);
        return user.get();
    }
    public User findById(Long userId){
        Optional<User> user = repository.findUserIdDeleteAtNull(userId, ConfigSystem.getTenant().getId());
        isPresent(user);
        return user.get();
    }

    public List<String> findUsersIdsByTenant(Long id) {
        List<String> userEmails = repository.findEmailsOfUsersByTenant(id);
        if(userEmails == null || userEmails.isEmpty()){
            return Collections.emptyList();
        }
        return userEmails;
    }
    public List<Long> getUserIdsBasedOnRole(User user) {
        List<Long> userIds = new ArrayList<>();

        if (isTenant(user)) {
            List<String> usersEmailsByTenant = findUsersIdsByTenant(user.getTenant().getId());
            List<Long> customersFromUser = customerService.findCustomerIdsFromAgencies(usersEmailsByTenant);
            userIds.addAll(customersFromUser);
        } else if (isAgency(user)) {
            List<Long> customersOfCurrentAgency = customerService.findCustomerIdsFromAgencies(Collections.singletonList(user.getEmail()));
            userIds.addAll(customersOfCurrentAgency);
        }

        return userIds;
    }
}
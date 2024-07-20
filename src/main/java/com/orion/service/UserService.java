package com.orion.service;

import com.orion.common.ResponseObject;
import com.orion.dto.UserData;
import com.orion.entity.User;
import com.orion.repository.UserRepository;
import com.orion.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
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

    public ResponseObject myProfile(Principal connectedUser) {
        String methodName = "getUserById";
        log.info("{} -> get user by id : {}", methodName);
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        ResponseObject responseObject = new ResponseObject();
        Optional<UserData> userData = repository.findUserDataById(user.getId());
        isPresent(userData);

        responseObject.setData(userData);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }
}
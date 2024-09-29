package com.orion.controller;

import com.orion.entity.User;
import com.orion.generics.ResponseObject;
import com.orion.dto.user.ChangePasswordRequest;
import com.orion.dto.user.UserData;
import com.orion.security.CustomUserDetails;
import com.orion.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/${base.url}/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get my profile", description = "Returns user by token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/me")
    public ResponseEntity myProfile(@AuthenticationPrincipal CustomUserDetails applicationUserDetails) {
        String methodName = "getMyProfile";

        log.info("{} -> Get my profile", methodName);
        ResponseObject responseObject = service.myProfile(applicationUserDetails.getUsername());

        log.info("{} -> Get my profile, response status: {}",methodName, responseObject.getCode());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }
}
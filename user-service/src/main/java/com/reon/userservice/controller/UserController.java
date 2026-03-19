package com.reon.userservice.controller;

import com.reon.exception.response.ApiResponse;
import com.reon.userservice.dto.RegistrationRequest;
import com.reon.userservice.dto.response.RegistrationResponse;
import com.reon.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> generateNewUser(@Valid @RequestBody RegistrationRequest registrationRequest){
        RegistrationResponse response = userService.registerUser(registrationRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        HttpStatus.CREATED,
                        "Profile created successfully.",
                        response
                ));
    }
}

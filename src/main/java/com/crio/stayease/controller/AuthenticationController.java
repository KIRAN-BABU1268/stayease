package com.crio.stayease.controller;

import com.crio.stayease.request.UserLoginDTO;
import com.crio.stayease.request.UserRegistrationDTO;
import com.crio.stayease.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        String token = authenticationService.register(registrationDTO);
        return ResponseEntity.ok("Registration successful. JWT Token: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO);
        return ResponseEntity.ok("Login successful. JWT Token: " + token);
    }
}


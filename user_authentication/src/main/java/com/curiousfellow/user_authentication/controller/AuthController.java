package com.curiousfellow.user_authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curiousfellow.user_authentication.requestDTO.LoginDTO;
import com.curiousfellow.user_authentication.requestDTO.RegisterDTO;
import com.curiousfellow.user_authentication.responseDTO.JwtResponse;
import com.curiousfellow.user_authentication.services.AuthService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    // Register New Users
    @PostMapping("/signup")
    public ResponseEntity<?> registerUsers(@RequestBody RegisterDTO entity) {
        return new ResponseEntity<>(authService.registerUser(entity), HttpStatus.CREATED);
    }

    // Login for Existing Users
    @PostMapping("/signin")
    public ResponseEntity<?> loginUsers(@RequestBody LoginDTO entity) {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(authService.loginUser(entity));
        jwtResponse.setMessage("Login Successful");

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    // Enable User
    @GetMapping("/confirm")
    public ResponseEntity<?> enableUser(@RequestParam("token") String token) {
        authService.enableUser(token);
        return ResponseEntity.ok("User Enabled");
    }
}

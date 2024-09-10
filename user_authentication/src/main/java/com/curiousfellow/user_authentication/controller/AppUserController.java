package com.curiousfellow.user_authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.curiousfellow.user_authentication.domain.AppUser;
import com.curiousfellow.user_authentication.services.AppUserService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AppUserController {

    private final AppUserService userService;

    @GetMapping("/profile")
    public ResponseEntity<AppUser> getMethodName(@AuthenticationPrincipal AppUser user) {
        return ResponseEntity.ok(userService.profile(user.getUsername()));
    }

}

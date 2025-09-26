package com.unitime.unitime_backend.controller;

import com.unitime.unitime_backend.dto.jwt.JwtResponseDTO;
import com.unitime.unitime_backend.dto.user.UserLoginDTO;
import com.unitime.unitime_backend.dto.user.UserRegisterDTO;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.service.AuthenticationService;
import com.unitime.unitime_backend.service.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO>  register(
            @RequestBody UserRegisterDTO body
            ) {
        JwtResponseDTO registredUser = authenticationService.register(body);
        return ResponseEntity.ok(registredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(
            @RequestBody UserLoginDTO body
            ) {
        JwtResponseDTO user = authenticationService.login(body);
        return ResponseEntity.ok(user);
    }
}

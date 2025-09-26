package com.unitime.unitime_backend.controller;

import com.unitime.unitime_backend.dto.user.UserRegisterDTO;
import com.unitime.unitime_backend.dto.user.UserResponseDTO;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }


    @PostMapping("/register")
    public UserResponseDTO createUser(@RequestBody UserRegisterDTO user) {
        return userService.createUser(user);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable long id,@RequestBody @Valid UserUpdateDTO request) {
//        try {
//            User updatedUser =
//        }
//    }
}
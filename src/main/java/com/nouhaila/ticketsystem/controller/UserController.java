package com.nouhaila.ticketsystem.controller;

import com.nouhaila.ticketsystem.dto.AuthDTO;
import com.nouhaila.ticketsystem.dto.UserDTO;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO authDTO) {
        try {
            userService.register(authDTO);
            return ResponseEntity.ok("Registration Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration Failed: " + e.getMessage());
        }
    }
    /**
     * Authenticate a user.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO authDTO) {
        User authenticatedUser = userService.authenticate(authDTO.getUsername(), authDTO.getPassword());
        if (authenticatedUser != null) {
            return ResponseEntity.ok("Login Successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    } 
  
}
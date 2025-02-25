package com.nouhaila.ticketsystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nouhaila.ticketsystem.dto.AuthDTO;
import com.nouhaila.ticketsystem.dto.UserDTO;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<UserDTO> getUserById(Long id) {
        System.out.println("Fetching user by ID: " + id);
        return userRepository.findById(id)
                .map(user -> {
                    System.out.println("User found: " + user.getUsername());
                    return new UserDTO(user.getId(), user.getUsername(), user.getRole());
                });
    }

    public void register(AuthDTO authDTO) {
        if (userRepository.findByUsername(authDTO.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(authDTO.getUsername());
        user.setPassword(authDTO.getPassword());
        user.setRole(authDTO.getRole());
        userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return user; // Return the authenticated user
            }
        }
        return null; // Authentication failed
    }
}
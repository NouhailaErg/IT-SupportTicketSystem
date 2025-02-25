package com.nouhaila.ticketsystem.service;

import com.nouhaila.ticketsystem.dto.UserDTO;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setPassword("encodedPassword");
        user.setRole(Role.EMPLOYEE);

        userDTO = new UserDTO(1L, "test_user", Role.EMPLOYEE);
    }

   
}

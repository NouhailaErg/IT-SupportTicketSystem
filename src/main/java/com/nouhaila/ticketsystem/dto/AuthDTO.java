package com.nouhaila.ticketsystem.dto;

import com.nouhaila.ticketsystem.enums.Role;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {
    private String username;
    private String password;
    private Role role; 
}

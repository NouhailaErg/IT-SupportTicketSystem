package com.nouhaila.ticketsystem.dto;

import lombok.*;
import com.nouhaila.ticketsystem.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private Long id;
	private String username;
	private Role role;
}

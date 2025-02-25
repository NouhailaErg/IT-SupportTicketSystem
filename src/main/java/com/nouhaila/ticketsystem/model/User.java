package com.nouhaila.ticketsystem.model;

import jakarta.persistence.*;
import lombok.*;
import com.nouhaila.ticketsystem.enums.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable= false)
	private String username;
	
	@Column(nullable= false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public User(long i, String username, Role role) {
        this.id = i;
        this.username = username;
        this.role = role;
    }
}


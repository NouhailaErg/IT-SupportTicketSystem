package com.nouhaila.ticketsystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import lombok.*;
import java.util.*;
import com.nouhaila.ticketsystem.enums.*;

@Entity
@Table(name ="tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
	@SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(nullable=false)
	private String title;
	
	@Column(nullable=false)
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Enumerated(EnumType.STRING)
	private Category category;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Status status = Status.NEW;
	
	@Column(name = "created_at",updatable = false)
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@ManyToOne
	@JoinColumn(name= "user_id")
	private User user;
	
	@OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	 private List<Comment> comments;

}
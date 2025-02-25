package com.nouhaila.ticketsystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String action;
	
	@Column(nullable=false, updatable=false)
	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="ticket_id", nullable=false)
	private Ticket ticket;
}

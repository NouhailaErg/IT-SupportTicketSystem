package com.nouhaila.ticketsystem.model;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "comment_text", nullable=false , length=4000)
	private String content;
	
	@Column(name = "comment_date", nullable=false, updatable=false)
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@ManyToOne
	@JoinColumn(name= "commented_by", nullable=true)  // Allow NULL when user is deleted
	private User user;
	
	@ManyToOne
	@JoinColumn(name="ticket_id", nullable=false)
	private Ticket ticket;
	
}

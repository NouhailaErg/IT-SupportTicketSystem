package com.nouhaila.ticketsystem.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.nouhaila.ticketsystem.enums.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
	private Long id;
	private String title;
	private String description;
	private Priority priority;
	private Category category;
	private Status status;
	private LocalDateTime createdAt;
	private Long userId;
	private List<CommentDTO> comments;
	
	
}

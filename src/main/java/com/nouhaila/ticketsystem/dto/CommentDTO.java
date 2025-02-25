package com.nouhaila.ticketsystem.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
	private Long id;
	private String content;
	private LocalDateTime createdAt;
	private Long userId;
}

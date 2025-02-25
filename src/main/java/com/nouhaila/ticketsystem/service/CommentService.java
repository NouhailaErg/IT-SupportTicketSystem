package com.nouhaila.ticketsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.nouhaila.ticketsystem.exception.*;

import com.nouhaila.ticketsystem.repository.CommentRepository;
import com.nouhaila.ticketsystem.repository.TicketRepository;
import com.nouhaila.ticketsystem.repository.UserRepository;
import com.nouhaila.ticketsystem.dto.CommentDTO;
import com.nouhaila.ticketsystem.model.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    
    public List<CommentDTO> getCommentsByTicketId(Long ticketId){
    	return commentRepository.findByTicketId(ticketId).stream().map(comment -> CommentDTO.builder()
    			.id(comment.getId())
    			.content(comment.getContent())
    			.createdAt(comment.getCreatedAt())
    			.userId(comment.getUser().getId())
    			.build()).collect(Collectors.toList());
    }
    
    public CommentDTO addComment(Long ticketId, Long userId, String content) {
    	Ticket ticket =  ticketRepository.findById(ticketId)
    			.orElseThrow(()-> new RunTimeException("Ticket not found"));
    	User user = userRepository.findById(userId)
    			.orElseThrow(()-> new RunTimeException("User not found"));
    	
    	Comment comment = Comment.builder()
    			.content(content)
    			.ticket(ticket)
    			.user(user)
    			.build();
    	
    	Comment savedComment = commentRepository.save(comment);
    	return CommentDTO.builder()
    			.id(savedComment.getId())
    			.content(savedComment.getContent())
    			.createdAt(savedComment.getCreatedAt())
    			.userId(savedComment.getUser().getId())
    			.build();
    }  

}

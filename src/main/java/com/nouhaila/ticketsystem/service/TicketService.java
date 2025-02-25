package com.nouhaila.ticketsystem.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouhaila.ticketsystem.dto.CommentDTO;
import com.nouhaila.ticketsystem.dto.TicketDTO;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.enums.Status;
import com.nouhaila.ticketsystem.exception.RunTimeException;
import com.nouhaila.ticketsystem.model.Ticket;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.repository.CommentRepository;
import com.nouhaila.ticketsystem.repository.TicketRepository;
import com.nouhaila.ticketsystem.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id).map(this::convertToDTO);
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        User user = userRepository.findById(ticketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = Ticket.builder()
                .title(ticketDTO.getTitle())
                .description(ticketDTO.getDescription())
                .priority(ticketDTO.getPriority())
                .category(ticketDTO.getCategory())
                .status(Status.NEW) // Set default status
                .user(user)
                .build();

        if (ticket.getPriority() == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    public void updateTicketStatus(Long ticketId, Status status, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Status oldStatus = ticket.getStatus(); // Get the current status before updating
        ticket.setStatus(status);
        ticketRepository.save(ticket);

        // Log the status change
        auditLogService.logTicketStatusChange(ticket, user, oldStatus, status);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .category(ticket.getCategory())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .userId(ticket.getUser().getId())
                .comments(
                    Hibernate.isInitialized(ticket.getComments()) ?
                    ticket.getComments().stream().map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .userId(comment.getUser().getId())
                        .build()).collect(Collectors.toList()) 
                    : Collections.emptyList()
                )
                .build();
    }

    @Transactional
    public List<TicketDTO> getTicketsByUserId(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Transactional
    public List<TicketDTO> getAllTicketsForUser(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

}
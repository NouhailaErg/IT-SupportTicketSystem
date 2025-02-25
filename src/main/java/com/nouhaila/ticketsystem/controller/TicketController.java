package com.nouhaila.ticketsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.nouhaila.ticketsystem.dto.*;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.enums.Status;
import com.nouhaila.ticketsystem.model.User;


import java.util.Optional;

import javax.validation.Valid;

import com.nouhaila.ticketsystem.service.CommentService;
import com.nouhaila.ticketsystem.service.TicketService;
import com.nouhaila.ticketsystem.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE') or hasRole('IT_SUPPORT')")
public class TicketController {
	
	@Autowired
	TicketService ticketService;
	@Autowired
    UserService userService; 
	@Autowired
	CommentService commentService;

    /**
     * Retrieves a list of all tickets or tickets by user ID.
     * IT_SUPPORT can see all tickets, while EMPLOYEE can only view their own.
     */
   @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'IT_SUPPORT')")
    public List<TicketDTO> getAllTickets(@AuthenticationPrincipal User user) {
        if (user.getRole() == Role.IT_SUPPORT) {
            System.out.println("Fetching all tickets for IT_SUPPORT...");
            List<TicketDTO> tickets = ticketService.getAllTickets();
            System.out.println("Tickets found: " + tickets.size());
            return tickets; 
        }
        return ticketService.getAllTicketsForUser(user.getId());
    }

    /**
     * Retrieves a specific ticket by ID.
     * EMPLOYEE can only access their own tickets, IT_SUPPORT can access all tickets.
     */

   @GetMapping("/{id}")
   @PreAuthorize("hasAnyRole('EMPLOYEE', 'IT_SUPPORT')")
   public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id, @AuthenticationPrincipal User user) {
       Optional<TicketDTO> ticket = ticketService.getTicketById(id);
       if (ticket.isPresent()) {
           if (user.getRole() == Role.EMPLOYEE && !ticket.get().getUserId().equals(user.getId())) {
               return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
           }
           return ResponseEntity.ok(ticket.get());
       }
       return ResponseEntity.notFound().build();
   }


    /**
     * Creates a new ticket. Only accessible by EMPLOYEE.
     */
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        TicketDTO createdTicket = ticketService.createTicket(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    /**
     * Updates the status of an existing ticket. Only accessible by IT_SUPPORT.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('IT_SUPPORT')")
    public ResponseEntity<Void> updateTicketStatus(@PathVariable Long id,   @RequestParam Status status,   @RequestParam Long userId) {
        try {
            ticketService.updateTicketStatus(id, status, userId);
            return ResponseEntity.noContent().build(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Add a comment by IT_SUPPORT.
     */
    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long ticketId, @RequestParam Long userId, @RequestParam String content) {
        CommentDTO commentDTO = commentService.addComment(ticketId, userId, content);
        return ResponseEntity.ok(commentDTO);
    }
    /**
     * Get all comments related to one specific ticket by IT_SUPPORT.
     */
    @GetMapping("/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long ticketId) {
        return ResponseEntity.ok(commentService.getCommentsByTicketId(ticketId));
    }

}

package com.nouhaila.ticketsystem.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouhaila.ticketsystem.dto.CommentDTO;
import com.nouhaila.ticketsystem.dto.TicketDTO;
import com.nouhaila.ticketsystem.dto.UserDTO;
import com.nouhaila.ticketsystem.enums.*;
import com.nouhaila.ticketsystem.service.*;

import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.service.TicketService;
import com.nouhaila.ticketsystem.service.UserService;
import com.nouhaila.ticketsystem.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTickets_EmployeeRole_ReturnsOwnTickets() {
        // Arrange
        User user = new User();
        user.setRole(Role.EMPLOYEE);
        user.setId(1L);

        List<TicketDTO> tickets = Arrays.asList(
                new TicketDTO(1L, "Ticket 1", "Description 1", Priority.LOW, Category.SOFTWARE, Status.NEW, null, 1L, null),
                new TicketDTO(2L, "Ticket 2", "Description 2", Priority.MEDIUM, Category.HARDWARE, Status.IN_PROGRESS, null, 1L, null)
        );
        when(ticketService.getAllTicketsForUser(1L)).thenReturn(tickets);

        // Act
        List<TicketDTO> result = ticketController.getAllTickets(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Ticket 1", result.get(0).getTitle());
        assertEquals("Ticket 2", result.get(1).getTitle());

        // Verify that the service method was called
        verify(ticketService, times(1)).getAllTicketsForUser(1L);
    }

    @Test
    void getAllTickets_ITSupportRole_ReturnsAllTickets() {
        // Arrange
        User user = new User();
        user.setRole(Role.IT_SUPPORT);

        List<TicketDTO> tickets = Arrays.asList(
                new TicketDTO(1L, "Ticket 1", "Description 1", Priority.LOW, Category.SOFTWARE, Status.NEW, null, 1L, null),
                new TicketDTO(2L, "Ticket 2", "Description 2", Priority.MEDIUM, Category.HARDWARE, Status.IN_PROGRESS, null, 2L, null)
        );
        when(ticketService.getAllTickets()).thenReturn(tickets);

        // Act
        List<TicketDTO> result = ticketController.getAllTickets(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Ticket 1", result.get(0).getTitle());
        assertEquals("Ticket 2", result.get(1).getTitle());

        // Verify that the service method was called
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    void getTicketById_EmployeeRole_Forbidden() {
        // Arrange
        User user = new User();
        user.setRole(Role.EMPLOYEE);
        user.setId(1L);

        TicketDTO ticketDTO = new TicketDTO(1L, "Ticket 1", "Description 1", Priority.LOW, Category.SOFTWARE, Status.NEW, null, 2L, null);
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(ticketDTO));

        // Act
        ResponseEntity<TicketDTO> response = ticketController.getTicketById(1L, user);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        // Verify that the service method was called
        verify(ticketService, times(1)).getTicketById(1L);
    }

    @Test
    void getTicketById_ITSupportRole_ReturnsTicket() {
        // Arrange
        User user = new User();
        user.setRole(Role.IT_SUPPORT);

        TicketDTO ticketDTO = new TicketDTO(1L, "Ticket 1", "Description 1", Priority.LOW, Category.SOFTWARE, Status.NEW, null, 2L, null);
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(ticketDTO));

        // Act
        ResponseEntity<TicketDTO> response = ticketController.getTicketById(1L, user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ticket 1", response.getBody().getTitle());

        // Verify that the service method was called
        verify(ticketService, times(1)).getTicketById(1L);
    }

    @Test
    void createTicket_EmployeeRole_ReturnsCreatedTicket() {
        // Arrange
        TicketDTO ticketDTO = new TicketDTO(null, "New Ticket", "New Description", Priority.MEDIUM, Category.HARDWARE, Status.NEW, null, 1L, null);
        TicketDTO createdTicket = new TicketDTO(1L, "New Ticket", "New Description", Priority.MEDIUM, Category.HARDWARE, Status.NEW, null, 1L, null);
        when(ticketService.createTicket(ticketDTO)).thenReturn(createdTicket);

        // Act
        ResponseEntity<TicketDTO> response = ticketController.createTicket(ticketDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Ticket", response.getBody().getTitle());

        // Verify that the service method was called
        verify(ticketService, times(1)).createTicket(ticketDTO);
    }

    @Test
    void updateTicketStatus_ITSupportRole_ReturnsNoContent() {
        // Arrange
        doNothing().when(ticketService).updateTicketStatus(1L, Status.IN_PROGRESS, 1L);

        // Act
        ResponseEntity<Void> response = ticketController.updateTicketStatus(1L, Status.IN_PROGRESS, 1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify that the service method was called
        verify(ticketService, times(1)).updateTicketStatus(1L, Status.IN_PROGRESS, 1L);
    }

    @Test
    void addComment_ReturnsComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO(1L, "Test comment", LocalDateTime.now(), 1L);
        when(commentService.addComment(1L, 1L, "Test comment")).thenReturn(commentDTO);

        // Act
        ResponseEntity<CommentDTO> response = ticketController.addComment(1L, 1L, "Test comment");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test comment", response.getBody().getContent());

        // Verify that the service method was called
        verify(commentService, times(1)).addComment(1L, 1L, "Test comment");
    }

    @Test
    void getComments_ReturnsComments() {
        // Arrange
        List<CommentDTO> comments = Arrays.asList(
                new CommentDTO(1L, "Comment 1", LocalDateTime.now(), 1L),
                new CommentDTO(2L, "Comment 2", LocalDateTime.now(), 2L)
        );
        when(commentService.getCommentsByTicketId(1L)).thenReturn(comments);

        // Act
        ResponseEntity<List<CommentDTO>> response = ticketController.getComments(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Comment 1", response.getBody().get(0).getContent());

        // Verify that the service method was called
        verify(commentService, times(1)).getCommentsByTicketId(1L);
    }
}



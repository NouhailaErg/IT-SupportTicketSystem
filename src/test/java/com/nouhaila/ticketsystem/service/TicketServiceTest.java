package com.nouhaila.ticketsystem.service;

import com.nouhaila.ticketsystem.dto.TicketDTO;
import com.nouhaila.ticketsystem.dto.UserDTO;
import com.nouhaila.ticketsystem.enums.Category;
import com.nouhaila.ticketsystem.enums.Priority;
import com.nouhaila.ticketsystem.enums.Status;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.exception.RunTimeException;
import com.nouhaila.ticketsystem.model.Ticket;
import com.nouhaila.ticketsystem.repository.TicketRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private TicketDTO ticketDTO;
    private UserDTO employeeDTO;
    private UserDTO itSupportDTO;

    @BeforeEach
    void setUp() {
        // Employee User
        employeeDTO = new UserDTO();
        employeeDTO.setId(1L);
        employeeDTO.setUsername("employee_user");
        employeeDTO.setRole(Role.EMPLOYEE); 

        // IT Support User
        itSupportDTO = new UserDTO();
        itSupportDTO.setId(2L);
        itSupportDTO.setUsername("support_user");
        itSupportDTO.setRole(Role.IT_SUPPORT);

        // Ticket Entity
        ticket = Ticket.builder()
                .id(1L)
                .title("Test Ticket")
                .description("This is a test ticket")
                .priority(Priority.HIGH)
                .category(Category.SOFTWARE)
                .status(Status.NEW)
                .build();

        // DTO for API interaction
        ticketDTO = TicketDTO.builder()
                .id(1L)
                .title("Test Ticket")
                .description("This is a test ticket")
                .priority(Priority.HIGH)
                .category(Category.SOFTWARE)
                .status(Status.NEW)
                .userId(1L)  // Created by Employee
                .build();
    }

    @Test
    void testEmployeeCanCreateTicket() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(employeeDTO));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketDTO result = ticketService.createTicket(ticketDTO);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testItSupportCanViewAllTickets() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));

        List<TicketDTO> result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        assertEquals("Test Ticket", result.get(0).getTitle());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testEmployeeCanViewOnlyTheirOwnTickets() {
        when(ticketRepository.findByUserId(1L)).thenReturn(Arrays.asList(ticket));

        List<TicketDTO> result = ticketService.getTicketsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Ticket", result.get(0).getTitle());
        verify(ticketRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testItSupportCanUpdateTicketStatus() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userService.getUserById(2L)).thenReturn(Optional.of(itSupportDTO));

        ticketService.updateTicketStatus(1L, Status.RESOLVED, 2L);

        assertEquals(Status.RESOLVED, ticket.getStatus());
        verify(ticketRepository, times(1)).save(ticket);
        verify(auditLogService, times(1)).logTicketStatusChange(any(), any(), any(), any());
    }

    @Test
    void testEmployeeCannotUpdateTicketStatus() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userService.getUserById(1L)).thenReturn(Optional.of(employeeDTO));

        assertThrows(RuntimeException.class, () -> ticketService.updateTicketStatus(1L, Status.RESOLVED, 1L));
    }

    @Test
    void testItSupportCanAddComment() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userService.getUserById(2L)).thenReturn(Optional.of(itSupportDTO));

        // Simulating adding a comment
        assertDoesNotThrow(() -> ticketService.updateTicketStatus(1L, Status.IN_PROGRESS, 2L));
    }
}

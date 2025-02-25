package com.nouhaila.ticketsystem.controller;

import com.nouhaila.ticketsystem.model.AuditLog;
import com.nouhaila.ticketsystem.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogControllerTest {

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditLogController auditLogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuditLogsByTicketId_ReturnsListOfAuditLogs() {
        // Arrange
        Long ticketId = 1L;
        AuditLog log1 = new AuditLog();
        log1.setId(1L);
        log1.setAction("Status changed from NEW to IN_PROGRESS");

        AuditLog log2 = new AuditLog();
        log2.setId(2L);
        log2.setAction("Comment added to the ticket");

        List<AuditLog> auditLogs = Arrays.asList(log1, log2);

        when(auditLogService.getAuditLogsByTicketId(ticketId)).thenReturn(auditLogs);

        // Act
        ResponseEntity<List<AuditLog>> response = auditLogController.getAuditLogsByTicketId(ticketId);

        // Assert
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("Status changed from NEW to IN_PROGRESS", response.getBody().get(0).getAction());
        assertEquals(2L, response.getBody().get(1).getId());
        assertEquals("Comment added to the ticket", response.getBody().get(1).getAction());

        // Verify that the service method was called
        verify(auditLogService, times(1)).getAuditLogsByTicketId(ticketId);
    }

    @Test
    void getAuditLogsByTicketId_ReturnsEmptyList() {
        // Arrange
        Long ticketId = 1L;
        when(auditLogService.getAuditLogsByTicketId(ticketId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<AuditLog>> response = auditLogController.getAuditLogsByTicketId(ticketId);

        // Assert
        assertTrue(response.getBody().isEmpty());

        // Verify that the service method was called
        verify(auditLogService, times(1)).getAuditLogsByTicketId(ticketId);
    }
}
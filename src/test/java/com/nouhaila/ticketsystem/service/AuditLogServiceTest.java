package com.nouhaila.ticketsystem.service;

import com.nouhaila.ticketsystem.enums.Status;
import com.nouhaila.ticketsystem.model.AuditLog;
import com.nouhaila.ticketsystem.model.Ticket;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    private Ticket ticket;
    private User user;
    private AuditLog auditLog;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        ticket = Ticket.builder()
                .id(1L)
                .title("Test Ticket")
                .status(Status.NEW)
                .build();

        auditLog = new AuditLog();
        auditLog.setTicket(ticket);
        auditLog.setUser(user);
        auditLog.setAction("Test Action");
        auditLog.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testLogTicketStatusChange() {
        // Call the method
        auditLogService.logTicketStatusChange(ticket, user, Status.NEW, Status.RESOLVED);

        // Verify repository is called
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testLogCommentAdded() {
        // Call the method
        auditLogService.logCommentAdded(ticket, user);

        // Verify repository is called
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testAuditLogDetailsAreCorrect() {
        doAnswer(invocation -> {
            AuditLog log = invocation.getArgument(0);
            assertEquals(ticket.getId(), log.getTicket().getId());
            assertEquals(user.getId(), log.getUser().getId());
            assertNotNull(log.getTimestamp());
            assertTrue(log.getAction().contains("Status changed"));
            return null;
        }).when(auditLogRepository).save(any(AuditLog.class));

        // Call the method
        auditLogService.logTicketStatusChange(ticket, user, Status.NEW, Status.RESOLVED);

        // Verify repository is called
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testGetAuditLogsByTicketId() {
        when(auditLogRepository.findByTicketId(1L)).thenReturn(Arrays.asList(auditLog));

        List<AuditLog> result = auditLogService.getAuditLogsByTicketId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Action", result.get(0).getAction());
        verify(auditLogRepository, times(1)).findByTicketId(1L);
    }

    @Test
    void testGetAuditLogsByTicketId_AccessDenied() {
        // Simulate a security check failure (since `@PreAuthorize` is used)
        doThrow(new AccessDeniedException("Access Denied"))
                .when(auditLogRepository).findByTicketId(1L);

        assertThrows(AccessDeniedException.class, () -> auditLogService.getAuditLogsByTicketId(1L));
    }
}

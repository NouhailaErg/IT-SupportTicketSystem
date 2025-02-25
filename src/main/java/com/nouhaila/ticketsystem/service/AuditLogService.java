package com.nouhaila.ticketsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.nouhaila.ticketsystem.enums.Status;
import com.nouhaila.ticketsystem.model.*;
import com.nouhaila.ticketsystem.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {
	private final AuditLogRepository auditLogRepository;
	
	public void logTicketStatusChange(Ticket ticket, User user, Status oldStatus, Status newStatus) {
        String action = "Status changed from " + oldStatus + " to " + newStatus;
        saveAuditLog(ticket, user, action);
    }

	public void logCommentAdded(Ticket ticket, User user) {
		String action= "Comment added to the ticket";
		saveAuditLog(ticket, user, action);
	}
	
	@PreAuthorize("hasRole('IT_SUPPORT')")
	public List<AuditLog> getAuditLogsByTicketId(Long ticketId){
		return auditLogRepository.findByTicketId(ticketId);
	}	
		
	private void saveAuditLog(Ticket ticket, User user, String action) {
		AuditLog log = new AuditLog();
		log.setTicket(ticket);
		log.setUser(user);
		log.setAction(action);
		log.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(log);
		
	}
	
}

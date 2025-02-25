package com.nouhaila.ticketsystem.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouhaila.ticketsystem.model.AuditLog;
import com.nouhaila.ticketsystem.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
	
	private final AuditLogService auditLogService;
	
	@GetMapping("/{ticketId}")
	public ResponseEntity<List<AuditLog>> getAuditLogsByTicketId(@PathVariable Long ticketId){
		List<AuditLog> logs = auditLogService.getAuditLogsByTicketId(ticketId);
		return ResponseEntity.ok(logs);
	}
}

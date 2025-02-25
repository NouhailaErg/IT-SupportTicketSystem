package com.nouhaila.ticketsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nouhaila.ticketsystem.model.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{
	List<AuditLog> findByTicketId(Long ticketId);
}

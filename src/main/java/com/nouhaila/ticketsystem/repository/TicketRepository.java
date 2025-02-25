package com.nouhaila.ticketsystem.repository;

import java.io.ObjectInputFilter.Status;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nouhaila.ticketsystem.model.Ticket;

public interface TicketRepository extends JpaRepository <Ticket, Long> {
	List<Ticket> findByStatus(Status status);
	List<Ticket> findByUserId(Long userId);
	List<Ticket> findAll();
}

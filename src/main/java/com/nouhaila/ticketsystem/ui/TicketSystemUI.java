package com.nouhaila.ticketsystem.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import com.nouhaila.ticketsystem.controller.AuditLogController;
import com.nouhaila.ticketsystem.controller.TicketController;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.model.User;

import java.awt.*;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class TicketSystemUI extends JFrame {
    private User user;
    private TicketController ticketController;
    private AuditLogController auditLogController;

    public TicketSystemUI(User user, TicketController ticketController, AuditLogController auditLogController) {
        this.user = user;
        this.ticketController = ticketController;
        this.auditLogController = auditLogController;
        setTitle("IT Support Ticket System - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill"));

        // Create tabs based on the user's role
        JTabbedPane tabbedPane = new JTabbedPane();

        if (user.getRole() == Role.EMPLOYEE) {
            // Employee features
            tabbedPane.addTab("Create Ticket", new CreateTicketPanel(user, ticketController));
            tabbedPane.addTab("View My Tickets", new ViewTicketsPanel(user, ticketController));
        } else if (user.getRole() == Role.IT_SUPPORT) {
            // IT Support features
            tabbedPane.addTab("View All Tickets", new ViewTicketsPanel(user, ticketController));
            tabbedPane.addTab("Audit Logs", new AuditLogPanel(auditLogController));
        }

        add(tabbedPane, "grow");
    }
}
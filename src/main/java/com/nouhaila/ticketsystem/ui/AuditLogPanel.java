package com.nouhaila.ticketsystem.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.springframework.http.ResponseEntity;

import java.util.List;
import com.nouhaila.ticketsystem.controller.*;
import com.nouhaila.ticketsystem.model.AuditLog;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AuditLogPanel extends JPanel {
    private final AuditLogController auditLogController;
    private JTable logTable;
    private DefaultTableModel tableModel;

    public AuditLogPanel(AuditLogController auditLogController) {
        this.auditLogController = auditLogController;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new MigLayout("wrap", "[grow]", "[][grow]"));

        // Customize the background color
        setBackground(new Color(245, 245, 245)); // Light gray background

        // Search panel
        JPanel searchPanel = new JPanel(new MigLayout("wrap", "[][grow]", "[]"));
        searchPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        JLabel ticketIdLabel = new JLabel("Ticket ID:");
        ticketIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        ticketIdLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        searchPanel.add(ticketIdLabel);

        JTextField ticketIdField = new JTextField(10);
        ticketIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        ticketIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light gray border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));
        searchPanel.add(ticketIdField, "growx");

        JButton searchButton = createStyledButton("Search", new Color(70, 130, 180)); // Steel blue
        searchPanel.add(searchButton, "wrap");

        add(searchPanel, "growx");

        // Table for displaying audit logs
        tableModel = new DefaultTableModel(new Object[]{"Ticket ID", "Action", "Changed By", "Change Date"}, 0);
        logTable = new JTable(tableModel);
        logTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logTable.setRowHeight(25); // Increase row height
        logTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        logTable.setSelectionForeground(Color.BLACK);
        JScrollPane tableScrollPane = new JScrollPane(logTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(tableScrollPane, "grow");

        // Event listener for the search button
        searchButton.addActionListener(e -> {
            try {
                Long ticketId = Long.parseLong(ticketIdField.getText());
                ResponseEntity<List<AuditLog>> response = auditLogController.getAuditLogsByTicketId(ticketId);
                List<AuditLog> logs = response.getBody();

                if (logs != null && !logs.isEmpty()) {
                    tableModel.setRowCount(0); // Clear existing rows
                    for (AuditLog log : logs) {
                        tableModel.addRow(new Object[]{
                                log.getTicket().getId(), // Ticket ID
                                log.getAction(), // Action
                                log.getUser().getUsername(), // Changed By (username)
                                log.getTimestamp() // Change Date
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No audit logs found for the given ticket ID.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ticket ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}

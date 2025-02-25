package com.nouhaila.ticketsystem.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.springframework.http.ResponseEntity;

import com.nouhaila.ticketsystem.controller.*;
import com.nouhaila.ticketsystem.dto.*;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.enums.Status;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.service.CommentService;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import net.miginfocom.swing.MigLayout;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewTicketsPanel extends JPanel {
    private final TicketController ticketController;
    private final User user;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusComboBox;
    private JTextArea commentTextArea;
    private JButton updateStatusButton, addCommentButton, viewDetailsButton;

    public ViewTicketsPanel(User user, TicketController ticketController) {
        this.ticketController = ticketController;
        this.user = user;
        setLayout(new MigLayout("wrap", "[grow]", "[][grow][][][grow][]"));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        setBackground(new Color(245, 245, 245)); 

        // Table for displaying tickets
        tableModel = new DefaultTableModel(new Object[]{"ID", "User ID", "Title", "Status", "Priority"}, 0);
        ticketTable = new JTable(tableModel);
        ticketTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ticketTable.setRowHeight(25); // Increase row height
        ticketTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        ticketTable.setSelectionForeground(Color.BLACK);
        JScrollPane tableScrollPane = new JScrollPane(ticketTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(tableScrollPane, "grow");

        // Refresh button
        JButton refreshButton = createStyledButton("Refresh", new Color(100, 149, 237)); // Cornflower blue
        add(refreshButton, "growx, align center");

        // View Details button
        viewDetailsButton = createStyledButton("View Details", new Color(60, 179, 113)); // Medium sea green
        add(viewDetailsButton, "growx");

        // Status dropdown (only visible for IT_SUPPORT)
        if (user.getRole() == Role.IT_SUPPORT) {
            statusComboBox = new JComboBox<>(new String[]{"NEW", "IN_PROGRESS", "RESOLVED"});
            statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statusComboBox.setBackground(Color.WHITE);
            add(new JLabel("Change Status:"), "align right");
            add(statusComboBox, "growx");

            // Update status button (only visible for IT_SUPPORT)
            updateStatusButton = createStyledButton("Update Status", new Color(255, 165, 0)); // Orange
            add(updateStatusButton, "growx");
        }

        // Comment text area (only visible for IT_SUPPORT)
        if (user.getRole() == Role.IT_SUPPORT) {
            add(new JLabel("Add Comment:"), "align right");
            commentTextArea = new JTextArea(3, 20);
            commentTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            commentTextArea.setLineWrap(true);
            commentTextArea.setWrapStyleWord(true);
            JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
            commentScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
            add(commentScrollPane, "grow");

            // Add comment button (only visible for IT_SUPPORT)
            addCommentButton = createStyledButton("Add Comment", new Color(70, 130, 180)); // Steel blue
            add(addCommentButton, "growx");
        }

        // Event listeners
        refreshButton.addActionListener(e -> refreshTickets());
        viewDetailsButton.addActionListener(e -> viewTicketDetails());

        if (user.getRole() == Role.IT_SUPPORT) {
            updateStatusButton.addActionListener(e -> updateTicketStatus());
            addCommentButton.addActionListener(e -> addCommentToTicket());
        }

        refreshTickets(); // Load tickets on initialization
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        return button;
    }

    private void refreshTickets() {
        tableModel.setRowCount(0); // Clear existing rows
        List<TicketDTO> tickets = ticketController.getAllTickets(user);

        if (tickets != null) {
            for (TicketDTO ticket : tickets) {
                tableModel.addRow(new Object[]{
                        ticket.getId(),
                        ticket.getUserId(),
                        ticket.getTitle(),
                        ticket.getStatus(),
                        ticket.getPriority()
                });
            }
        }
    }

    private void viewTicketDetails() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to view details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long ticketId = (Long) tableModel.getValueAt(selectedRow, 0);
        ResponseEntity<TicketDTO> response = ticketController.getTicketById(ticketId, user);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Open a new dialog to display ticket details and comments
            new TicketDetailsPanel(response.getBody(), ticketController, user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load ticket details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTicketStatus() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to update the status.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long ticketId = (Long) tableModel.getValueAt(selectedRow, 0);
        String statusString = (String) statusComboBox.getSelectedItem();

        try {
            Status newStatus = Status.valueOf(statusString.toUpperCase());
            ResponseEntity<Void> response = ticketController.updateTicketStatus(ticketId, newStatus, user.getId());

            if (response.getStatusCode().is2xxSuccessful()) {
                JOptionPane.showMessageDialog(this, "Ticket status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTickets();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update ticket status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid status selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCommentToTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to add a comment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long ticketId = (Long) tableModel.getValueAt(selectedRow, 0);
        String comment = commentTextArea.getText().trim();
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Comment cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ResponseEntity<CommentDTO> response = ticketController.addComment(ticketId, user.getId(), comment);
        if (response.getStatusCode().is2xxSuccessful()) {
            JOptionPane.showMessageDialog(this, "Comment added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            commentTextArea.setText(""); // Clear input
            refreshTickets();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add comment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
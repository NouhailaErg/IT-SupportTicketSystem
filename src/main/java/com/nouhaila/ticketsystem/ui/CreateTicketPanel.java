package com.nouhaila.ticketsystem.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import org.springframework.http.ResponseEntity;

import com.nouhaila.ticketsystem.dto.TicketDTO;
import com.nouhaila.ticketsystem.enums.*;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.controller.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class CreateTicketPanel extends JPanel {
    private final User user;
    private final TicketController ticketController;

    public CreateTicketPanel(User user, TicketController ticketController) {
        this.user = user;
        this.ticketController = ticketController;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new MigLayout("wrap 2", "[right][grow, fill]", "[][][][][][grow]"));

        //background color
        setBackground(new Color(240, 240, 240)); // Light gray background

        // Title
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        titleLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(titleLabel);

        JTextField titleField = new JTextField(20);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));
        add(titleField, "growx");

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        descriptionLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(descriptionLabel);

        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light gray border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));
        add(scrollPane, "grow");

        // Priority
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        priorityLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(priorityLabel);

        JComboBox<Priority> priorityComboBox = new JComboBox<>(Priority.values());
        priorityComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityComboBox.setSelectedItem(Priority.MEDIUM); // Set a default value
        add(priorityComboBox, "growx");

        // Category
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(categoryLabel);

        JComboBox<Category> categoryComboBox = new JComboBox<>(Category.values());
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setSelectedItem(Category.HARDWARE); // Set a default value
        add(categoryComboBox, "growx");

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false); // Remove focus border
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        add(submitButton, "span 2, align center, gaptop 20");

        submitButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            Priority priority = (Priority) priorityComboBox.getSelectedItem();
            Category category = (Category) categoryComboBox.getSelectedItem();

            // Validate input
            if (title.isEmpty() || description.isEmpty() || priority == null || category == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Build the TicketDTO
            TicketDTO ticketDTO = TicketDTO.builder()
                    .title(title)
                    .description(description)
                    .priority(priority)
                    .category(category)
                    .userId(user.getId()) // Set the user ID
                    .build();

            // Send the request to create the ticket
            ResponseEntity<TicketDTO> response = ticketController.createTicket(ticketDTO);
            TicketDTO createdTicket = response.getBody();

            if (createdTicket != null) {
                JOptionPane.showMessageDialog(this, "Ticket created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create ticket", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
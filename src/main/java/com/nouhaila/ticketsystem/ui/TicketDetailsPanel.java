package com.nouhaila.ticketsystem.ui;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.springframework.http.ResponseEntity;

import com.nouhaila.ticketsystem.controller.TicketController;
import com.nouhaila.ticketsystem.dto.CommentDTO;
import com.nouhaila.ticketsystem.dto.TicketDTO;
import com.nouhaila.ticketsystem.model.*;
import com.nouhaila.ticketsystem.service.TicketService;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TicketDetailsPanel extends JDialog {
    private final TicketDTO ticket;
    private final TicketController ticketController;
    private final User user;
    private JTextArea ticketDetailsArea;
    private JTextArea commentTextArea;
    private JButton addCommentButton;
    private JTextArea commentsArea;

    public TicketDetailsPanel(TicketDTO ticket, TicketController ticketController, User user) {
        this.ticket = ticket;
        this.ticketController = ticketController;
        this.user = user;

        setTitle("Ticket Details");
        setSize(700, 500); // Larger dialog for better readability
        setLayout(new MigLayout("wrap", "[grow]", "[][grow][][grow][]"));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        // Ticket details
        ticketDetailsArea = new JTextArea(5, 40);
        ticketDetailsArea.setEditable(false);
        ticketDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ticketDetailsArea.setBackground(new Color(255, 255, 255)); // White background
        ticketDetailsArea.setText(
                "ID: " + ticket.getId() + "\n" +
                "Title: " + ticket.getTitle() + "\n" +
                "Description: " + ticket.getDescription() + "\n" +
                "Status: " + ticket.getStatus() + "\n" +
                "Priority: " + ticket.getPriority() + "\n" +
                "Created At: " + ticket.getCreatedAt()
        );
        add(new JScrollPane(ticketDetailsArea), "grow");

        // Existing comments
        add(new JLabel("Comments:"), "align right");
        commentsArea = new JTextArea(10, 40);
        commentsArea.setEditable(false);
        commentsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        commentsArea.setBackground(new Color(255, 255, 255)); // White background
        JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
        commentsScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(commentsScrollPane, "grow");

        // Add comment
        add(new JLabel("Add Comment:"), "align right");
        commentTextArea = new JTextArea(3, 40);
        commentTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
        commentScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(commentScrollPane, "grow");

        // Add comment button
        addCommentButton = new JButton("Add Comment");
        addCommentButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        addCommentButton.setBackground(new Color(70, 130, 180)); // Steel blue
        addCommentButton.setForeground(Color.WHITE);
        addCommentButton.setFocusPainted(false);
        addCommentButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        add(addCommentButton, "growx");

        // Event listeners
        addCommentButton.addActionListener(e -> addComment());

        loadComments(); // Load existing comments
    }

    private void loadComments() {
        ResponseEntity<List<CommentDTO>> response = ticketController.getComments(ticket.getId());
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            StringBuilder commentsText = new StringBuilder();
            for (CommentDTO comment : response.getBody()) {
                commentsText.append("User: ").append(comment.getUserId())
                        .append(" - ").append(comment.getContent())
                        .append(" (").append(comment.getCreatedAt()).append(")\n");
            }
            commentsArea.setText(commentsText.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load comments.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addComment() {
        String comment = commentTextArea.getText().trim();
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Comment cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ResponseEntity<CommentDTO> response = ticketController.addComment(ticket.getId(), user.getId(), comment);
        if (response.getStatusCode().is2xxSuccessful()) {
            JOptionPane.showMessageDialog(this, "Comment added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            commentTextArea.setText(""); // Clear input
            loadComments(); // Refresh comments
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add comment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
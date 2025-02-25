package com.nouhaila.ticketsystem.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.nouhaila.ticketsystem.TicketsystemApplication;
import com.nouhaila.ticketsystem.controller.AuditLogController;
import com.nouhaila.ticketsystem.controller.TicketController;
import com.nouhaila.ticketsystem.enums.Role;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.repository.UserRepository;
import com.nouhaila.ticketsystem.service.UserService;

import java.awt.*;
import java.awt.event.*;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private final UserService userService;
    private final TicketController ticketController;
    private final AuditLogController auditLogController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage(UserService userService, TicketController ticketController, AuditLogController auditLogController) {
        this.userService = userService;
        this.ticketController = ticketController;
        this.auditLogController = auditLogController;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Login");
        setSize(500, 300); // Adjusted window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Set the icon for the title bar
        ImageIcon icon = new ImageIcon("support.png"); // Load the icon from the project root
        setIconImage(icon.getImage()); // Set the icon in the title bar

        // Use a modern layout with padding
        setLayout(new MigLayout("wrap 2", "[][grow, fill]", "[][][][grow]"));

        // Customize the background color
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // Add a title label
        JLabel titleLabel = new JLabel("Ticket System Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(titleLabel, "span 2, align center, gapbottom 20");

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(usernameLabel, "align right");
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light gray border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));
        add(usernameField, "growx");

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(50, 50, 50)); // Dark gray color
        add(passwordLabel, "align right");
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light gray border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));
        add(passwordField, "growx");

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setFocusPainted(false); // Remove focus border
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        add(loginButton, "span 2, align center, gaptop 20");

        // Add action listener for the login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = userService.authenticate(username, password);
            if (user != null) {
                dispose(); // Close login window
                new TicketSystemUI(user, ticketController, auditLogController).setVisible(true); // Open the main UI
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TicketsystemApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        TicketController ticketController = context.getBean(TicketController.class);
        AuditLogController auditLogController = context.getBean(AuditLogController.class);

        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage(userService, ticketController, auditLogController);
            loginPage.setVisible(true);
        });
    }
}
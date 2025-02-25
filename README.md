IT Support Ticket System

Overview

The IT Support Ticket System is a web-based application designed to streamline IT issue tracking and resolution. It allows users to create, manage, and track tickets while providing IT teams with tools to monitor progress, audit changes, and communicate effectively.

Features

User Authentication: Secure login system.

Ticket Management: Create, update, and track tickets.

Status Updates: Change ticket statuses (Open, In Progress, Resolved) using a dropdown menu.

Audit Logging: Automatic logging of ticket actions for accountability.

Comments: Add and view comments for better collaboration.

Search & Filter: Easily find tickets based on criteria.

Role-Based Access: Control actions based on user roles.

Docker Deployment: Containerized application for easy deployment.

Technologies Used

Backend: Java 17, Spring Boot, RESTful API (Swagger/OpenAPI)

Frontend: Java Swing

Database: Oracle SQL

Containerization: Docker

Installation & Setup

Prerequisites

Java 17

Maven

Docker

Oracle Database

Steps

Clone the repository:

git clone https://github.com/yourusername/it-support-ticket-system.git
cd it-support-ticket-system

Set up the database:

Create an Oracle database schema.

Update the application properties with database credentials.

Build the project:

mvn clean install

Run the application:

java -jar target/it-support-ticket-system.jar

Run with Docker:

docker-compose up -d

Usage

Login Page: Users authenticate with credentials.

Dashboard: View active tickets and summaries.

Create Ticket: Submit a new issue.

Update Status: Change ticket status via a dropdown (logs changes automatically).

Commenting: Add updates or notes to a ticket.

Audit Logs: Track ticket history for accountability.

Contributing

Contributions are welcome! Feel free to fork the repository and submit a pull request.

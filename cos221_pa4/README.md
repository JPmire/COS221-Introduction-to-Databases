# Practical 4

This module contains Practical 4 for the Introduction to Databases (COS 221) course.

## Overview
This practical demonstrates interacting with the **Chinook** database using a Java application via JDBC. The project leverages Data Access Objects (DAOs) and Data Models to cleanly separate business logic from the database layer.

## Setup Instructions

### 1. Database Initialization
This project uses the Chinook sample database on MySQL.
1. Open **MySQL Workbench**.
2. Execute the `Chinook_MySql.sql` script to create the `u25056809_chinook` database and populate the tables with data.

### 2. Project Configuration
Ensure that your database connection settings (URL, Username, Password) are correctly configured inside the `src/main/java/org/example/util/DatabaseManager.java` (or wherever your database configuration resides).

### 3. Build & Run
The application is built with **Maven**. 
- To compile the project: 
  ```sh
  mvn clean install
  ```
- To run the main application:
  ```sh
  mvn exec:java -Dexec.mainClass="org.example.Main"
  ```
  *(Or execute `Main.java` / `Runner.java` directly from your IDE, like IntelliJ IDEA or Eclipse).*

## Technologies Used
- Java (JDK)
- JDBC (Java Database Connectivity)
- Maven
- MySQL Server & MySQL Workbench

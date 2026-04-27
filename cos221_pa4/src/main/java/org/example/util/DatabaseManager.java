package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    public static Connection getConnection() throws SQLException {
        // Reads from the Environment Variables you set up in IntelliJ's Run Configuration
        String proto = System.getenv("CHINOOK_DB_PROTO");
        String host = System.getenv("CHINOOK_DB_HOST");
        String port = System.getenv("CHINOOK_DB_PORT");
        String name = System.getenv("CHINOOK_DB_NAME");
        String user = System.getenv("CHINOOK_DB_USERNAME");
        String pass = System.getenv("CHINOOK_DB_PASSWORD");

        // Builds the connection URL: jdbc:mysql://127.0.0.1:3306/chinook
        String url = proto + "://" + host + ":" + port + "/" + name;

        // Connects to MySQL and returns the connection
        return DriverManager.getConnection(url, user, pass);
    }
}
package org.example.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final Map<String, String> envVars = new HashMap<>();

    static {
        String[] candidates = {
            ".env",
            "practical-4/.env",
            System.getProperty("user.dir") + "/.env",
            System.getProperty("user.dir") + "/practical-4/.env"
        };
        for (String path : candidates) {
            if (new java.io.File(path).exists()) {
                loadEnvFile(path);
                break;
            }
        }
    }

    private static void loadEnvFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int eq = line.indexOf('=');
                if (eq > 0) {
                    envVars.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
                }
            }
        } catch (IOException ignored) {
            // .env file not found — fall back to system environment variables
        }
    }

    private static String getEnv(String key, String defaultValue) {
        String val = System.getenv(key);
        if (val != null && !val.isEmpty()) return val;
        val = envVars.get(key);
        if (val != null && !val.isEmpty()) return val;
        return defaultValue;
    }

    public static Connection getConnection() throws SQLException {
        String proto = getEnv("CHINOOK_DB_PROTO", "jdbc:mysql");
        String host  = getEnv("CHINOOK_DB_HOST",  "127.0.0.1");
        String port  = getEnv("CHINOOK_DB_PORT",  "3306");
        String name  = getEnv("CHINOOK_DB_NAME",  "chinook");
        String user  = getEnv("CHINOOK_DB_USERNAME", "root");
        String pass  = getEnv("CHINOOK_DB_PASSWORD", "");

        String url = proto + "://" + host + ":" + port + "/" + name;
        return DriverManager.getConnection(url, user, pass);
    }
}
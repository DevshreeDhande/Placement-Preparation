package com.ghristu.placeprep.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URI;

public class DBConnection {

    private static String jdbcUrl;
    private static String dbUser;
    private static String dbPassword;

    static {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            // Railway format: postgresql://user:password@host:port/dbname
            try {
                URI uri = new URI(databaseUrl.replace("postgresql://", "http://"));
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();
                String[] userInfo = uri.getUserInfo().split(":");
                jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                dbUser = userInfo[0];
                dbPassword = userInfo[1];
            } catch (Exception e) {
                System.err.println("Failed to parse DATABASE_URL: " + e.getMessage());
                jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
                dbUser = "postgres";
                dbPassword = "postgres";
            }
        } else {
            // Fallback to individual env vars
            jdbcUrl = databaseUrl != null ? databaseUrl :
                     (System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/postgres");
            dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") :
                    (System.getenv("PGUSER") != null ? System.getenv("PGUSER") : "postgres");
            dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") :
                        (System.getenv("PGPASSWORD") != null ? System.getenv("PGPASSWORD") : "postgres");
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }
}

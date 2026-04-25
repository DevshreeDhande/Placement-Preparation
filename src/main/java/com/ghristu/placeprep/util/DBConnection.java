package com.ghristu.placeprep.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Supabase PostgreSQL Connection details
    // Support for multiple environment variable names (Railway, Vercel, Supabase)
    private static final String URL = System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL") : 
                                     (System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/postgres");
    
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : 
                                      (System.getenv("PGUSER") != null ? System.getenv("PGUSER") : "postgres");
    
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : 
                                          (System.getenv("PGPASSWORD") != null ? System.getenv("PGPASSWORD") : "postgres");

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }
}

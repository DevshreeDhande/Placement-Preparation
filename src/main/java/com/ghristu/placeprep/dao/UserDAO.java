package com.ghristu.placeprep.dao;

import com.ghristu.placeprep.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    public Map<String, Object> getUserByEmail(String email) {
        String query = "SELECT id, name, email, role, phone, is_active FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getString("id"));
                user.put("name", rs.getString("name"));
                user.put("email", rs.getString("email"));
                user.put("role", rs.getString("role"));
                user.put("phone", rs.getString("phone"));
                user.put("isActive", rs.getBoolean("is_active"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateUser(String email, String plaintextPassword) {
        String query = "SELECT password_hash FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return org.mindrot.jbcrypt.BCrypt.checkpw(plaintextPassword, storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(String email, String name, String role, String plaintextPassword) {
        String query = "INSERT INTO users (name, email, role, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(plaintextPassword, org.mindrot.jbcrypt.BCrypt.gensalt(12));
            
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setString(4, hashed);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.ghristu.placeprep.dao;

import com.ghristu.placeprep.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackDAO {

    public List<Map<String, Object>> getAllFeedback() {
        List<Map<String, Object>> feedbacks = new ArrayList<>();
        String query = "SELECT f.id, f.category, f.message, f.response, TO_CHAR(f.created_at, 'YYYY-MM-DD HH24:MI:SS') as time, " +
                       "COALESCE(u.name, 'Anonymous') as name " +
                       "FROM feedback f " +
                       "LEFT JOIN users u ON f.user_id = u.id " +
                       "ORDER BY f.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> f = new HashMap<>();
                f.put("id", rs.getInt("id"));
                f.put("cat", rs.getString("category"));
                f.put("msg", rs.getString("message"));
                f.put("time", rs.getString("time"));
                f.put("response", rs.getString("response"));
                
                String name = rs.getString("name");
                f.put("name", name);
                
                String initials = "AN";
                if (name != null && !name.isEmpty() && !name.equals("Anonymous")) {
                    String[] parts = name.split(" ");
                    if (parts.length > 1) {
                        initials = (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
                    } else {
                        initials = parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
                    }
                }
                f.put("initials", initials);
                
                feedbacks.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    public boolean addFeedback(int userId, String category, String message) {
        String query = "INSERT INTO feedback (user_id, category, message) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            if (userId > 0) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, category);
            ps.setString(3, message);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

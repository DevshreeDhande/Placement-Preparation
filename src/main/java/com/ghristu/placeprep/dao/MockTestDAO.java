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

public class MockTestDAO {

    public List<Map<String, Object>> getAllMockTests(int userId) {
        List<Map<String, Object>> tests = new ArrayList<>();
        String query = "SELECT m.id, m.name, m.duration_min, m.total_questions, m.difficulty, m.category, m.description, m.icon, " +
                       "COALESCE(t.status, 'new') as status, t.score, TO_CHAR(m.created_at, 'YYYY-MM-DD') as date " +
                       "FROM mock_tests m " +
                       "LEFT JOIN test_attempts t ON m.id = t.test_id AND t.user_id = ? " +
                       "ORDER BY m.id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> t = new HashMap<>();
                t.put("id", rs.getInt("id"));
                t.put("name", rs.getString("name"));
                t.put("time", rs.getInt("duration_min") + " min");
                t.put("duration", rs.getInt("duration_min"));
                t.put("questions", rs.getInt("total_questions"));
                t.put("difficulty", rs.getString("difficulty"));
                t.put("category", rs.getString("category"));
                t.put("topics", rs.getString("category")); // For frontend compatibility
                t.put("desc", rs.getString("description"));
                t.put("icon", rs.getString("icon"));
                t.put("status", rs.getString("status"));
                t.put("date", rs.getString("date"));
                
                int score = rs.getInt("score");
                if (!rs.wasNull()) {
                    t.put("score", score);
                }
                
                tests.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    public boolean createMockTest(String name, int durationMin, int totalQuestions, String difficulty, String category, String description, String icon) {
        String query = "INSERT INTO mock_tests (name, duration_min, total_questions, difficulty, category, description, icon) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, name);
            ps.setInt(2, durationMin);
            ps.setInt(3, totalQuestions);
            ps.setString(4, difficulty);
            ps.setString(5, category);
            ps.setString(6, description);
            ps.setString(7, icon);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

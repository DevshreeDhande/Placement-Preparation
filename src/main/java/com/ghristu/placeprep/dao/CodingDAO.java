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

public class CodingDAO {

    public List<Map<String, Object>> getAllProblems(int userId) {
        List<Map<String, Object>> problems = new ArrayList<>();
        String query = "SELECT c.id, c.title, c.difficulty, c.tags, c.link, " +
                       "(CASE WHEN u.problem_id IS NOT NULL THEN true ELSE false END) as solved " +
                       "FROM coding_problems c " +
                       "LEFT JOIN user_coding_progress u ON c.id = u.problem_id AND u.user_id = ? " +
                       "ORDER BY c.id ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("id", rs.getInt("id"));
                p.put("title", rs.getString("title"));
                p.put("difficulty", rs.getString("difficulty"));
                
                String tagsStr = rs.getString("tags");
                if (tagsStr != null && !tagsStr.isEmpty()) {
                    p.put("tags", tagsStr.split(","));
                } else {
                    p.put("tags", new String[]{});
                }
                
                p.put("link", rs.getString("link"));
                p.put("solved", rs.getBoolean("solved"));
                problems.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return problems;
    }

    public boolean setProblemSolvedStatus(int userId, int problemId, boolean isSolved) {
        if (isSolved) {
            String query = "INSERT INTO user_coding_progress (user_id, problem_id) VALUES (?, ?) ON CONFLICT (user_id, problem_id) DO NOTHING";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setInt(2, problemId);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String query = "DELETE FROM user_coding_progress WHERE user_id = ? AND problem_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setInt(2, problemId);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

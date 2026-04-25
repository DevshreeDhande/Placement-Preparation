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

public class CodingProblemDAO {

    public List<Map<String, Object>> getAllProblems() {
        List<Map<String, Object>> problems = new ArrayList<>();
        String query = "SELECT id, title, difficulty_level, tags FROM coding_problems WHERE is_active = true";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("id", rs.getInt("id"));
                p.put("title", rs.getString("title"));
                p.put("difficulty", rs.getString("difficulty_level"));
                // In PostgreSQL, array maps to array/json, assuming parsed string here
                p.put("tags", new String[]{ rs.getString("tags") }); 
                p.put("solved", false);
                p.put("link", "#");
                problems.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return problems;
    }
}

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

public class CompanyDAO {

    public List<Map<String, Object>> getAllCompanies() {
        List<Map<String, Object>> companies = new ArrayList<>();
        String query = "SELECT id, name, type, icon, ctc, openings FROM companies WHERE is_active = true";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", rs.getInt("id"));
                c.put("name", rs.getString("name"));
                c.put("type", rs.getString("type"));
                c.put("icon", rs.getString("icon"));
                c.put("ctc", rs.getString("ctc"));
                c.put("openings", rs.getInt("openings"));
                // Additional roadmap fetches would happen here
                companies.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }
}

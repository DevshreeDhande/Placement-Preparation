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
                c.put("tags", new String[]{rs.getString("type")}); // Basic tags array
                
                // Fetch Roadmap
                String roadmapQuery = "SELECT overview, process_steps, duration_weeks, key_topics, interview_tips FROM roadmaps WHERE company_id = ?";
                try (PreparedStatement roadmapPs = conn.prepareStatement(roadmapQuery)) {
                    roadmapPs.setInt(1, rs.getInt("id"));
                    ResultSet roadmapRs = roadmapPs.executeQuery();
                    if (roadmapRs.next()) {
                        Map<String, Object> roadmap = new HashMap<>();
                        roadmap.put("overview", roadmapRs.getString("overview"));
                        roadmap.put("process", roadmapRs.getString("process_steps"));
                        
                        String weeksRaw = roadmapRs.getString("duration_weeks");
                        roadmap.put("weeks", weeksRaw != null ? weeksRaw.split("\\|") : new String[0]);
                        
                        String topicsRaw = roadmapRs.getString("key_topics");
                        roadmap.put("topics", topicsRaw != null ? topicsRaw.split(",") : new String[0]);
                        
                        String tipsRaw = roadmapRs.getString("interview_tips");
                        roadmap.put("tips", tipsRaw != null ? tipsRaw.split("\\|") : new String[0]);
                        
                        c.put("roadmap", roadmap);
                    }
                }
                companies.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public boolean addCompany(String name, String type, String icon, String ctc, int openings, String overview, String process, String duration, String topics, String tips) {
        String insertCompany = "INSERT INTO companies (name, type, icon, ctc, openings) VALUES (?, ?, ?, ?, ?) RETURNING id";
        String insertRoadmap = "INSERT INTO roadmaps (company_id, overview, process_steps, duration_weeks, key_topics, interview_tips) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction block
            
            try (PreparedStatement psCompany = conn.prepareStatement(insertCompany)) {
                psCompany.setString(1, name);
                psCompany.setString(2, type);
                psCompany.setString(3, icon);
                psCompany.setString(4, ctc);
                psCompany.setInt(5, openings);
                
                ResultSet rs = psCompany.executeQuery();
                if (rs.next()) {
                    int companyId = rs.getInt(1);
                    
                    try (PreparedStatement psRoadmap = conn.prepareStatement(insertRoadmap)) {
                        psRoadmap.setInt(1, companyId);
                        psRoadmap.setString(2, overview);
                        psRoadmap.setString(3, process);
                        psRoadmap.setString(4, duration);
                        psRoadmap.setString(5, topics);
                        psRoadmap.setString(6, tips);
                        psRoadmap.executeUpdate();
                    }
                    conn.commit();
                    return true;
                }
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

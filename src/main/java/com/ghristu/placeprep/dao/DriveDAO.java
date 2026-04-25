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

public class DriveDAO {

    public List<Map<String, Object>> getAllDrives() {
        List<Map<String, Object>> drives = new ArrayList<>();
        String query = "SELECT id, company_name, TO_CHAR(drive_date, 'YYYY-MM-DD') as drive_date, type, roles, ctc, status FROM placement_drives ORDER BY drive_date ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> d = new HashMap<>();
                d.put("id", rs.getInt("id"));
                d.put("company", rs.getString("company_name"));
                d.put("date", rs.getString("drive_date"));
                d.put("type", rs.getString("type"));
                d.put("roles", rs.getString("roles"));
                d.put("ctc", rs.getString("ctc"));
                d.put("status", rs.getString("status"));
                drives.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drives;
    }

    public boolean addDrive(String company, String date, String type, String roles, String ctc) {
        String query = "INSERT INTO placement_drives (company_name, drive_date, type, roles, ctc, status) VALUES (?, CAST(? AS DATE), ?, ?, ?, 'pending')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, company);
            ps.setString(2, date);
            ps.setString(3, type);
            ps.setString(4, roles);
            ps.setString(5, ctc);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteDrive(int id) {
        String query = "DELETE FROM placement_drives WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

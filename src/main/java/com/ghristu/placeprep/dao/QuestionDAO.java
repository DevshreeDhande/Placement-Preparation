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

public class QuestionDAO {

    public List<Map<String, Object>> getAddedQuestions() {
        List<Map<String, Object>> questions = new ArrayList<>();
        String query = "SELECT id, category, type, level, question_text, option_a, option_b, option_c, option_d, " +
                       "correct_option, formula, explanation " +
                       "FROM questions ORDER BY id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> q = new HashMap<>();
                q.put("id", rs.getInt("id"));
                q.put("category", rs.getString("category"));
                q.put("type", rs.getString("type"));
                q.put("difficulty", rs.getString("level"));
                q.put("question", rs.getString("question_text"));
                
                List<String> options = new ArrayList<>();
                options.add(rs.getString("option_a"));
                options.add(rs.getString("option_b"));
                String c = rs.getString("option_c");
                String d = rs.getString("option_d");
                if (c != null && !c.isEmpty()) options.add(c);
                if (d != null && !d.isEmpty()) options.add(d);
                q.put("options", options);
                
                q.put("correct", rs.getInt("correct_option"));
                q.put("formula", rs.getString("formula"));
                q.put("explanation", rs.getString("explanation"));
                
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public boolean addQuestion(String category, String type, String level, String questionText,
                               String optA, String optB, String optC, String optD,
                               int correctOption, String formula, String explanation) {
        String query = "INSERT INTO questions (category, type, level, question_text, option_a, option_b, option_c, option_d, correct_option, formula, explanation) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, category);
            ps.setString(2, type);
            ps.setString(3, level);
            ps.setString(4, questionText);
            ps.setString(5, optA);
            ps.setString(6, optB);
            ps.setString(7, optC != null ? optC : "");
            ps.setString(8, optD != null ? optD : "");
            ps.setInt(9, correctOption);
            ps.setString(10, formula);
            ps.setString(11, explanation);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

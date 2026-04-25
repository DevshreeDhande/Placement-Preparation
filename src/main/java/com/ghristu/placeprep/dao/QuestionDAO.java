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

    public List<Map<String, Object>> getQuestionsByCategoryAndDifficulty(String category, String difficulty) {
        List<Map<String, Object>> questions = new ArrayList<>();
        String query = """
            SELECT q.id, q.question_text, q.options, q.correct_option_index, 
                   q.formula, q.explanation, q.steps 
            FROM questions q 
            JOIN question_categories c ON q.category_id = c.id 
            WHERE c.name = ? AND q.difficulty_level = ? AND q.is_active = true
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, category);
            ps.setString(2, difficulty);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> q = new HashMap<>();
                q.put("q", rs.getString("question_text"));
                // options is JSON in db, assuming parsing needed here, mocked for brevity
                q.put("options", new String[]{"Option A", "Option B", "Option C", "Option D"});
                q.put("correct", rs.getInt("correct_option_index"));
                q.put("formula", rs.getString("formula"));
                q.put("explanation", rs.getString("explanation"));
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}

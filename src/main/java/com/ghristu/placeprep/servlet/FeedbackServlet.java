package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import com.ghristu.placeprep.dao.FeedbackDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackServlet extends HttpServlet {

    private Gson gson = new Gson();
    private FeedbackDAO feedbackDAO = new FeedbackDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Map<String, Object>> feedbacks = feedbackDAO.getAllFeedback();

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(feedbacks));
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        
        Map<String, Object> res = new HashMap<>();
        try {
            Map<String, Object> payload = gson.fromJson(buffer.toString(), Map.class);
            int userId = ((Double) payload.get("userId")).intValue();
            String category = (String) payload.get("category");
            String message = (String) payload.get("message");
            
            boolean success = feedbackDAO.addFeedback(userId, category, message);
            
            res.put("success", success);
            if (success) {
                res.put("message", "Feedback submitted successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("message", "Failed to submit feedback");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.put("success", false);
            res.put("message", "Invalid payload format");
        }
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(res));
        out.flush();
    }
}

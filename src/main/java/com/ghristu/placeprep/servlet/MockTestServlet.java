package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import com.ghristu.placeprep.dao.MockTestDAO;

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

public class MockTestServlet extends HttpServlet {

    private Gson gson = new Gson();
    private MockTestDAO mockTestDAO = new MockTestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int userId = 0;
        String userIdParam = request.getParameter("userId");
        if (userIdParam != null) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        List<Map<String, Object>> tests = mockTestDAO.getAllMockTests(userId);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(tests));
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
            String name = (String) payload.get("name");
            int duration = Integer.parseInt(payload.get("duration").toString());
            int questions = Integer.parseInt(payload.get("questions").toString());
            String difficulty = (String) payload.getOrDefault("difficulty", "medium");
            String category = (String) payload.getOrDefault("category", "custom");
            String desc = (String) payload.getOrDefault("desc", "Custom assessment");
            String icon = (String) payload.getOrDefault("icon", "📝");
            
            boolean success = mockTestDAO.createMockTest(name, duration, questions, difficulty, category, desc, icon);
            
            res.put("success", success);
            if (success) {
                res.put("message", "Mock test created successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("message", "Failed to create mock test");
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

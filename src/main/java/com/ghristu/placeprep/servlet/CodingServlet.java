package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import com.ghristu.placeprep.dao.CodingDAO;

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

public class CodingServlet extends HttpServlet {

    private Gson gson = new Gson();
    private CodingDAO codingDAO = new CodingDAO();

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
                // Ignore and use 0
            }
        }

        List<Map<String, Object>> problems = codingDAO.getAllProblems(userId);
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(problems));
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
            int problemId = ((Double) payload.get("problemId")).intValue();
            boolean isSolved = (Boolean) payload.get("solved");
            
            boolean success = codingDAO.setProblemSolvedStatus(userId, problemId, isSolved);
            
            res.put("success", success);
            if (success) {
                res.put("message", "Progress updated");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("message", "Failed to update progress");
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

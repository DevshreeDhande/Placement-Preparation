package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import com.ghristu.placeprep.dao.QuestionDAO;

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

public class QuestionServlet extends HttpServlet {

    private Gson gson = new Gson();
    private QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Map<String, Object>> questions = questionDAO.getAddedQuestions();

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(questions));
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
            String category = (String) payload.get("category");
            String type = (String) payload.getOrDefault("type", "aptitude");
            String level = (String) payload.getOrDefault("difficulty", "beginner");
            String questionText = (String) payload.get("question");
            String optA = (String) payload.get("optA");
            String optB = (String) payload.get("optB");
            String optC = (String) payload.get("optC");
            String optD = (String) payload.get("optD");
            int correct = ((Double) payload.getOrDefault("correct", 0.0)).intValue();
            String explanation = (String) payload.getOrDefault("explanation", "");

            boolean success = questionDAO.addQuestion(category, type, level, questionText,
                    optA, optB, optC, optD, correct, "", explanation);

            res.put("success", success);
            if (success) {
                res.put("message", "Question added successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("message", "Failed to add question");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.put("success", false);
            res.put("message", "Invalid payload: " + e.getMessage());
        }

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(res));
        out.flush();
    }
}

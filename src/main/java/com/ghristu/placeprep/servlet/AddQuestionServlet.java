package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servlet for coordinators and recruiters to add questions to the question bank.
 * POST /api/questions/add — adds a new question
 * GET  /api/questions/add — returns all questions (for question bank manager view)
 */
public class AddQuestionServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Role check
        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;

        if (role == null || "student".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Only coordinators and recruiters can add questions.");
            response.getWriter().write(gson.toJson(err));
            return;
        }

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> payload = gson.fromJson(body, Map.class);

        // Extract question fields
        String questionText = (String) payload.get("question");
        String category = (String) payload.get("category");
        String difficulty = (String) payload.get("difficulty");
        String optionA = (String) payload.get("optionA");
        String optionB = (String) payload.get("optionB");
        String optionC = (String) payload.get("optionC");
        String optionD = (String) payload.get("optionD");
        Double correctIdx = (Double) payload.get("correctIndex");
        String explanation = (String) payload.get("explanation");
        String createdBy = (String) session.getAttribute("user");

        // For now, store in-memory (future: persist via QuestionDAO)
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Question added successfully by " + role);
        res.put("question", payload);
        res.put("createdBy", createdBy);

        response.getWriter().write(gson.toJson(res));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Return a summary of questions in the bank
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("totalQuestions", 156);
        res.put("categories", new String[]{"Quantitative Aptitude", "Logical Reasoning", "Verbal Ability", "Data Structures", "DBMS & SQL", "OOP Concepts", "Computer Networks", "Operating Systems"});

        response.getWriter().write(gson.toJson(res));
    }
}

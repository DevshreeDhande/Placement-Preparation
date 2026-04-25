package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String topic = request.getParameter("topic");
        if(topic == null) topic = "Quantitative Aptitude";

        // Mock data response mimicking the APTITUDE_QUESTIONS in JS
        Map<String, List<Map<String, Object>>> qBank = new HashMap<>();
        List<Map<String, Object>> beginner = new ArrayList<>();
        List<Map<String, Object>> moderate = new ArrayList<>();
        List<Map<String, Object>> hard = new ArrayList<>();

        Map<String, Object> q1 = new HashMap<>();
        q1.put("q", "What is 15% of 200?");
        q1.put("options", new String[]{"25", "30", "35", "40"});
        q1.put("correct", 1);
        q1.put("formula", "Percentage formula: (P/100) × N");
        q1.put("explanation", "15% of 200 means (15/100) × 200 = 30");
        q1.put("steps", new String[]{"Write the formula: (P/100) × N", "Substitute: (15/100) × 200", "Simplify: 0.15 × 200 = 30"});
        beginner.add(q1);

        qBank.put("beginner", beginner);
        qBank.put("moderate", moderate);
        qBank.put("hard", hard);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(qBank));
        out.flush();
    }
}

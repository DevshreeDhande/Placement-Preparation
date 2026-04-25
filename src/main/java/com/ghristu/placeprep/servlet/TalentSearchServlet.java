package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Servlet for recruiters to search and filter student talent.
 * GET /api/talent/search?skill=DSA&minScore=70 — returns filtered students
 */
public class TalentSearchServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Role check
        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;

        if (role == null || !"recruiter".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Only recruiters can access talent search.");
            response.getWriter().write(gson.toJson(err));
            return;
        }

        String skill = request.getParameter("skill");
        String minScoreStr = request.getParameter("minScore");
        int minScore = minScoreStr != null ? Integer.parseInt(minScoreStr) : 0;

        // Mock talent data (future: fetch from StudentDAO with filters)
        String[][] data = {
            {"Aarav Sharma", "CSE", "2025", "92", "DSA,DBMS,OOP"},
            {"Priya Patel", "IT", "2025", "88", "Aptitude,Reasoning,DBMS"},
            {"Rohit Gupta", "CSE", "2025", "85", "OOP,Networks,DSA"},
            {"Sneha Reddy", "ECE", "2025", "78", "DBMS,SQL,Aptitude"},
            {"Amit Kumar", "CSE", "2026", "74", "Aptitude,Verbal,DSA"},
            {"Neha Singh", "IT", "2026", "71", "Reasoning,Verbal,OOP"},
            {"Vikram Joshi", "ME", "2025", "65", "Aptitude,Networks"},
            {"Kavya Nair", "CSE", "2026", "60", "DBMS,SQL"}
        };

        List<Map<String, Object>> results = new ArrayList<>();
        for (String[] d : data) {
            int score = Integer.parseInt(d[3]);
            String skills = d[4];

            if (score < minScore) continue;
            if (skill != null && !skill.isEmpty() && !skills.toLowerCase().contains(skill.toLowerCase())) continue;

            Map<String, Object> s = new HashMap<>();
            s.put("name", d[0]);
            s.put("branch", d[1]);
            s.put("batch", d[2]);
            s.put("score", score);
            s.put("skills", d[4].split(","));
            results.add(s);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("totalResults", results.size());
        res.put("students", results);

        response.getWriter().write(gson.toJson(res));
    }
}

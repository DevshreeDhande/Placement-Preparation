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
 * Servlet for coordinators to view student analytics.
 * GET /api/analytics/students — returns all student performance data
 */
public class StudentAnalyticsServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Role check
        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;

        if (role == null || "student".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Only coordinators can view student analytics.");
            response.getWriter().write(gson.toJson(err));
            return;
        }

        // Mock student analytics data (future: fetch from StudentDAO)
        List<Map<String, Object>> students = new ArrayList<>();

        String[][] data = {
            {"Aarav Sharma", "CSE", "2025", "92", "DSA,DBMS", "Verbal", "14", "8"},
            {"Priya Patel", "IT", "2025", "88", "Aptitude,Reasoning", "OS", "12", "5"},
            {"Rohit Gupta", "CSE", "2025", "85", "OOP,Networks", "Aptitude", "11", "7"},
            {"Sneha Reddy", "ECE", "2025", "78", "DBMS", "DSA,Reasoning", "9", "4"},
            {"Amit Kumar", "CSE", "2026", "74", "Aptitude", "OOP,DBMS", "8", "3"},
            {"Neha Singh", "IT", "2026", "71", "Reasoning,Verbal", "Networks", "7", "6"},
            {"Vikram Joshi", "ME", "2025", "65", "Aptitude", "DSA,OOP", "6", "2"},
            {"Kavya Nair", "CSE", "2026", "60", "DBMS", "Aptitude,Reasoning", "5", "1"}
        };

        for (String[] d : data) {
            Map<String, Object> s = new HashMap<>();
            s.put("name", d[0]);
            s.put("branch", d[1]);
            s.put("batch", d[2]);
            s.put("score", Integer.parseInt(d[3]));
            s.put("strengths", d[4].split(","));
            s.put("weaknesses", d[5].split(","));
            s.put("tests", Integer.parseInt(d[6]));
            s.put("streak", Integer.parseInt(d[7]));
            students.add(s);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("totalStudents", students.size());
        res.put("students", students);

        response.getWriter().write(gson.toJson(res));
    }
}

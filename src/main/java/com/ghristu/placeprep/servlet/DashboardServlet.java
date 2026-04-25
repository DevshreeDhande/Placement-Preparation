package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DashboardServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : "student";
        
        Map<String, Object> dashData = new HashMap<>();
        if ("coordinator".equals(role)) {
            dashData.put("totalStudents", 450);
            dashData.put("placedStudents", 120);
            dashData.put("activeDrives", 5);
        } else if ("recruiter".equals(role)) {
            dashData.put("applicants", 85);
            dashData.put("shortlisted", 20);
            dashData.put("interviewsScheduled", 10);
        } else {
            // Student
            dashData.put("upcomingTests", 2);
            dashData.put("newCompanies", 4);
            dashData.put("dailyGoalProgress", 80);
        }

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(dashData));
        out.flush();
    }
}

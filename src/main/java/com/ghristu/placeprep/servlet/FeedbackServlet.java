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
import java.util.stream.Collectors;

public class FeedbackServlet extends HttpServlet {

    private Gson gson = new Gson();
    
    // In-memory feedback store for dev
    private static List<Map<String, String>> feedbacks = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(feedbacks));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, String> payload = gson.fromJson(body, Map.class);
        
        feedbacks.add(0, payload); // Add to front
        
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Feedback submitted successfully.");

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(res));
        out.flush();
    }
}

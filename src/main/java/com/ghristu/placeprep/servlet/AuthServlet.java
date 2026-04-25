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

public class AuthServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo(); // /login or /register
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        
        Map<String, String> payload = gson.fromJson(body, Map.class);
        Map<String, Object> res = new HashMap<>();

        if ("/login".equals(pathInfo)) {
            String email = payload.get("email");
            String password = payload.get("password");
            
            // Dummy authentication Logic
            if (email != null && !email.isEmpty() && password != null) {
                // Determine mock role based on email suffix or random logic
                String role = email.contains("coordinator") ? "coordinator" : (email.contains("recruiter") ? "recruiter" : "student");
                
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", email);
                session.setAttribute("role", role);

                Map<String, String> userMap = new HashMap<>();
                userMap.put("email", email);
                userMap.put("name", email.split("@")[0]);
                userMap.put("role", role);

                res.put("success", true);
                res.put("message", "Login successful");
                res.put("user", userMap);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.put("success", false);
                res.put("message", "Invalid credentials");
            }
        } else if ("/logout".equals(pathInfo)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            res.put("success", true);
            res.put("message", "Logged out successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.put("success", false);
            res.put("message", "Endpoint not found");
        }

        response.getWriter().write(gson.toJson(res));
    }
}

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

        com.ghristu.placeprep.dao.UserDAO userDAO = new com.ghristu.placeprep.dao.UserDAO();

        if ("/login".equals(pathInfo)) {
            String email = payload.get("email");
            String password = payload.get("password");
            
            if (email != null && password != null && userDAO.validateUser(email, password)) {
                Map<String, Object> userDetails = userDAO.getUserByEmail(email);
                String role = (String) userDetails.get("role");
                
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", email);
                session.setAttribute("role", role);

                res.put("success", true);
                res.put("message", "Login successful");
                res.put("user", userDetails);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.put("success", false);
                res.put("message", "Invalid credentials");
            }
        } else if ("/register".equals(pathInfo)) {
            String email = payload.get("email");
            String name = payload.get("name");
            String role = payload.get("role");
            String password = payload.get("password");
            
            if (userDAO.getUserByEmail(email) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                res.put("success", false);
                res.put("message", "Email already exists");
            } else if (userDAO.registerUser(email, name, role, password)) {
                res.put("success", true);
                res.put("message", "Registration successful");
                Map<String, Object> userDetails = userDAO.getUserByEmail(email);
                
                HttpSession session = request.getSession(true);
                session.setAttribute("user", email);
                session.setAttribute("role", role);
                res.put("user", userDetails);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("success", false);
                res.put("message", "Failed to register user");
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

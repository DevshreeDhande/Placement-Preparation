package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> payload = gson.fromJson(body, Map.class);
        
        // Mock PDF Generation logic (returns a success state)
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Resume generated successfully.");
        res.put("downloadUrl", "/api/resume/download?id=" + Math.random());

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(res));
        out.flush();
    }
}

package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyServlet extends HttpServlet {

    private Gson gson = new Gson();

    private com.ghristu.placeprep.dao.CompanyDAO companyDAO = new com.ghristu.placeprep.dao.CompanyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Map<String, Object>> companies = companyDAO.getAllCompanies();
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(companies));
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
        
        Map<String, Object> payload = gson.fromJson(buffer.toString(), Map.class);
        Map<String, Object> res = new HashMap<>();
        
        try {
            String name = (String) payload.get("name");
            String type = (String) payload.get("type");
            String icon = (String) payload.get("icon");
            String ctc = (String) payload.get("ctc");
            int openings = ((Double) payload.get("openings")).intValue(); // Gson parses numbers as Double
            
            // Roadmap parts
            String overview = (String) payload.get("overview");
            String process = (String) payload.get("process");
            String duration = (String) payload.get("duration");
            String topics = (String) payload.get("topics");
            String tips = (String) payload.get("tips");
            
            boolean success = companyDAO.addCompany(name, type, icon, ctc, openings, overview, process, duration, topics, tips);
            
            if (success) {
                res.put("success", true);
                res.put("message", "Company and Roadmap added successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("success", false);
                res.put("message", "Database error occurred");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.put("success", false);
            res.put("message", "Invalid payload format");
        }
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(res));
        out.flush();
    }
}

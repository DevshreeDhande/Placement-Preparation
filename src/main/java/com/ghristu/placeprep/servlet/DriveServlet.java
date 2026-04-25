package com.ghristu.placeprep.servlet;

import com.google.gson.Gson;
import com.ghristu.placeprep.dao.DriveDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriveServlet extends HttpServlet {

    private Gson gson = new Gson();
    private DriveDAO driveDAO = new DriveDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Map<String, Object>> drives = driveDAO.getAllDrives();
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(drives));
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
            String company = (String) payload.get("company");
            String date = (String) payload.get("date");
            String type = (String) payload.get("type");
            String roles = (String) payload.get("roles");
            String ctc = (String) payload.get("ctc");
            
            boolean success = driveDAO.addDrive(company, date, type, roles, ctc);
            
            if (success) {
                res.put("success", true);
                res.put("message", "Drive added successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.put("success", false);
                res.put("message", "Failed to add drive");
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        Map<String, Object> res = new HashMap<>();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                boolean success = driveDAO.deleteDrive(id);
                if (success) {
                    res.put("success", true);
                    res.put("message", "Drive removed successfully");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    res.put("success", false);
                    res.put("message", "Drive not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.put("success", false);
                res.put("message", "Invalid drive ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.put("success", false);
            res.put("message", "Drive ID required");
        }
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(res));
        out.flush();
    }
}

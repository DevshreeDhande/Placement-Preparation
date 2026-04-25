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

public class ProgressServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Temporary Mock Data
        Map<String, Object> progress = new HashMap<>();
        progress.put("score", 1250);
        progress.put("rank", 42);
        progress.put("aptitudeComplete", 85);
        progress.put("codingSolved", 34);
        progress.put("mockTestsTaken", 5);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(progress));
        out.flush();
    }
}

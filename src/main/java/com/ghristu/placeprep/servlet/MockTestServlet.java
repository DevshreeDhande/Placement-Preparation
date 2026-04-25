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

public class MockTestServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Map<String, Object>> tests = new ArrayList<>();
        
        Map<String, Object> t1 = new HashMap<>();
        t1.put("id", "M1");
        t1.put("name", "TCS NQT Full Mock");
        t1.put("category", "company");
        t1.put("icon", "🔵");
        t1.put("time", "120m");
        t1.put("questions", 92);
        t1.put("difficulty", "medium");
        t1.put("status", "pending");
        t1.put("desc", "Full-length cognitive and technical test mimicking actual TCS NQT.");
        tests.add(t1);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("id", "M2");
        t2.put("name", "Core DSA Assessment");
        t2.put("category", "tech");
        t2.put("icon", "💻");
        t2.put("time", "60m");
        t2.put("questions", 30);
        t2.put("difficulty", "hard");
        t2.put("status", "done");
        t2.put("score", 85);
        t2.put("desc", "Advanced algorithms and data structures test.");
        tests.add(t2);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(tests));
        out.flush();
    }
}

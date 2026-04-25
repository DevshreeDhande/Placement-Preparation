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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Temporary Mock Data Generation for the API
        List<Map<String, Object>> companies = new ArrayList<>();
        
        Map<String, Object> tcs = new HashMap<>();
        tcs.put("name", "TCS");
        tcs.put("type", "tech");
        tcs.put("icon", "🔵");
        tcs.put("tags", new String[]{"tech", "product"});
        tcs.put("ctc", "3.5–6 LPA");
        tcs.put("openings", 120);
        
        Map<String, Object> tcsRoadmap = new HashMap<>();
        tcsRoadmap.put("overview", "Tata Consultancy Services is India's largest IT services company.");
        tcsRoadmap.put("process", "Online Test (NQT) → Technical Interview → Managerial Interview → HR Round");
        tcsRoadmap.put("weeks", new String[]{
            "Week 1-2: Focus on Quantitative Aptitude",
            "Week 3-4: Verbal Ability",
            "Week 5-6: Programming Logic",
            "Week 7-8: Coding practice",
            "Week 9-10: Mock NQTs"
        });
        tcsRoadmap.put("topics", new String[]{"Quantitative Aptitude", "Verbal Ability", "Programming Logic", "Java/Python Basics"});
        tcsRoadmap.put("tips", new String[]{"TCS NQT has a strict timer", "Focus on accuracy"});
        tcs.put("roadmap", tcsRoadmap);
        companies.add(tcs);

        Map<String, Object> amz = new HashMap<>();
        amz.put("name", "Amazon");
        amz.put("type", "product");
        amz.put("icon", "🛒");
        amz.put("tags", new String[]{"tech", "product"});
        amz.put("ctc", "25–45 LPA");
        amz.put("openings", 80);
        
        Map<String, Object> amzRoadmap = new HashMap<>();
        amzRoadmap.put("overview", "Amazon is one of the highest-paying recruiters on campus.");
        amzRoadmap.put("process", "Online Assessment (2 coding) → Phone Screen → Onsite (4 rounds)");
        amzRoadmap.put("weeks", new String[]{
            "Week 1-3: Data Structures deep dive",
            "Week 4-6: Algorithm patterns",
            "Week 7-8: System design basics",
            "Week 9: Amazon Leadership Principles"
        });
        amzRoadmap.put("topics", new String[]{"Arrays", "Trees & Graphs", "System Design", "Leadership Principles"});
        amzRoadmap.put("tips", new String[]{"Every interview includes behavioral questions", "LeetCode medium is the sweet spot"});
        amz.put("roadmap", amzRoadmap);
        companies.add(amz);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(companies));
        out.flush();
    }
}

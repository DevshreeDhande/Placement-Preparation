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

public class CodingServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Temporary Mock Data for Coding Challenges
        List<Map<String, Object>> problems = new ArrayList<>();
        
        Map<String, Object> p1 = new HashMap<>();
        p1.put("title", "Two Sum");
        p1.put("tags", new String[]{"Arrays", "HashMap"});
        p1.put("difficulty", "Easy");
        p1.put("solved", true);
        p1.put("link", "#");
        problems.add(p1);

        Map<String, Object> p2 = new HashMap<>();
        p2.put("title", "Longest Substring Without Repeating Characters");
        p2.put("tags", new String[]{"Strings", "Sliding Window", "HashMap"});
        p2.put("difficulty", "Medium");
        p2.put("solved", false);
        p2.put("link", "#");
        problems.add(p2);

        Map<String, Object> p3 = new HashMap<>();
        p3.put("title", "Merge K Sorted Lists");
        p3.put("tags", new String[]{"Linked List", "Divide and Conquer", "Heap"});
        p3.put("difficulty", "Hard");
        p3.put("solved", false);
        p3.put("link", "#");
        problems.add(p3);

        Map<String, Object> p4 = new HashMap<>();
        p4.put("title", "Binary Tree Level Order Traversal");
        p4.put("tags", new String[]{"Trees", "BFS"});
        p4.put("difficulty", "Medium");
        p4.put("solved", true);
        p4.put("link", "#");
        problems.add(p4);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(problems));
        out.flush();
    }
}

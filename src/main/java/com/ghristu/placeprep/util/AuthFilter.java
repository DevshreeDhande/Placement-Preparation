package com.ghristu.placeprep.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Check for session token or attribute indicating an active login
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        // Alternatively check Authorization header for JWT token in a fully stateless API
        String authHeader = req.getHeader("Authorization");
        boolean hasToken = (authHeader != null && authHeader.startsWith("Bearer "));

        if (loggedIn || hasToken) {
            chain.doFilter(request, response);
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Unauthorized access. Please log in.\"}");
        }
    }

    @Override
    public void destroy() {
        // Cleanup
    }
}

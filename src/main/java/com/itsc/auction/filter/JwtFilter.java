package com.itsc.auction.filter;

import org.springframework.stereotype.Component;

import com.itsc.auction.Utils.JwtUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, java.io.IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = null;

        Cookie[] cookies = httpRequest.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // check if the request path is /auth/login
        Boolean isLogin = httpRequest.getRequestURI().equals("/auth/login");
     
        System.out.println("Token: " + token);
        if (token == null || !JwtUtil.validateToken(token) || isLogin) {
            httpResponse.sendRedirect("/auth/login"); 
            return;
        }
        

        httpRequest.setAttribute("user", JwtUtil.getClaims(token));
        chain.doFilter(request, response);
    }
}

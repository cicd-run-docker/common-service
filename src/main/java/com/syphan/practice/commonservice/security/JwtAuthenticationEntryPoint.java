package com.syphan.practice.commonservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syphan.practice.commonservice.util.OpenApiBaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        String requestURI = httpServletRequest.getRequestURI();
        OpenApiBaseResponse response = new OpenApiBaseResponse();
        if (requestURI.equals("/api/v1/auth/sign-in")) {
            response.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setCode("Login.Error");
            response.setMessage("Username or password is incorrect.");
        } else {
            response.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setCode("Unauthorized");
            response.setMessage("Please login first.");
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setStatus(HttpStatus.valueOf(response.getHttpStatusCode()).value());
        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(response));
        httpServletResponse.flushBuffer();
    }
}

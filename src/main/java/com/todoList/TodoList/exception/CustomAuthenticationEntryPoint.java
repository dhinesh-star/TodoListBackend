package com.todoList.TodoList.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoList.TodoList.responseDTO.ErrorResponseDTO;
import com.todoList.TodoList.transformer.ErrorResponseDTOTransformer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setHeader("TodoList-denied-reason", "Authentication Failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTOTransformer.errorResponseDTOTransformer("Authentication Failed");
        String jsonValue = new ObjectMapper().writeValueAsString(errorResponseDTO);
        response.getWriter().write(jsonValue);
    }
}

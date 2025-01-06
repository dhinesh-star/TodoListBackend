package com.todoList.TodoList.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoList.TodoList.responseDTO.ErrorResponseDTO;
import com.todoList.TodoList.transformer.ErrorResponseDTOTransformer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setHeader("TodoList-denied-reason", "Access Denied Error");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTOTransformer.errorResponseDTOTransformer("Access Denied");
        String jsonValue = new ObjectMapper().writeValueAsString(errorResponseDTO);
        response.getWriter().write(jsonValue);
    }
}

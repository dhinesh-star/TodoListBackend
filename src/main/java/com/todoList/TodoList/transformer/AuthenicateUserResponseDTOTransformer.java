package com.todoList.TodoList.transformer;

import com.todoList.TodoList.responseDTO.AuthenicateUserResponseDTO;

public class AuthenicateUserResponseDTOTransformer {
    public static AuthenicateUserResponseDTO authenicateUserResponseDTO(String userName, String jwtToken){
        AuthenicateUserResponseDTO authenicateUserResponseDTO = AuthenicateUserResponseDTO.builder()
                .userName(userName)
                .token(jwtToken)
                .build();
        return authenicateUserResponseDTO;
    }
}
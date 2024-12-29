package com.todoList.TodoList.controller;

import com.todoList.TodoList.requestDTO.AddNewUserDTO;
import com.todoList.TodoList.requestDTO.LoginRequestDTO;
import com.todoList.TodoList.responseDTO.AddNewUserResponseDTO;
import com.todoList.TodoList.responseDTO.AuthenicateUserResponseDTO;
import com.todoList.TodoList.responseDTO.ErrorResponseDTO;
import com.todoList.TodoList.service.UserService;
import com.todoList.TodoList.transformer.AddNewUserResponseDTOTransformer;
import com.todoList.TodoList.transformer.AuthenicateUserResponseDTOTransformer;
import com.todoList.TodoList.transformer.ErrorResponseDTOTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/add")
    public ResponseEntity addNewUser(@RequestBody AddNewUserDTO addNewUserDTO){
        try {
            String response = userService.addNewTask(addNewUserDTO);
            AddNewUserResponseDTO addNewUserResponseDTO = AddNewUserResponseDTOTransformer.addNewUserResponseDTO(addNewUserDTO.getUserName(), response);
            log.info(addNewUserResponseDTO.toString());
            return new ResponseEntity<>(addNewUserResponseDTO, HttpStatus.CREATED);
        }catch (Exception e){
            ErrorResponseDTO errorResponseDTO = ErrorResponseDTOTransformer.errorResponseDTOTransformer(e.getMessage());
            log.warn(errorResponseDTO.toString());
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity authenicateUser(@RequestBody LoginRequestDTO loginRequestDTO){
        try {
            String responseJWTToken = userService.authenicateUser(loginRequestDTO.username(), loginRequestDTO.password());
            AuthenicateUserResponseDTO authenicateUserResponseDTO = AuthenicateUserResponseDTOTransformer.authenicateUserResponseDTO(loginRequestDTO.username(), responseJWTToken);
            log.info(authenicateUserResponseDTO.toString());
            return new ResponseEntity<>(authenicateUserResponseDTO, HttpStatus.OK);
        }catch (Exception e){
            ErrorResponseDTO errorResponseDTO = ErrorResponseDTOTransformer.errorResponseDTOTransformer(e.getMessage());
            log.warn(errorResponseDTO.toString());
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.OK);
        }
    }
}

package com.todoList.TodoList.entity;

import com.todoList.TodoList.enumPackage.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    private String emailId;

    @Indexed(unique = true)
    private String userName;

    private String encodedPassword;

    private Set<Roles> rolesSet;
}

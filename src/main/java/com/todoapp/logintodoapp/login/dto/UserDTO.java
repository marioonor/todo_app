package com.todoapp.logintodoapp.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    
    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String role;

}
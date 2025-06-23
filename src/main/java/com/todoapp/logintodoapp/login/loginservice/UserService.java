package com.todoapp.logintodoapp.login.loginservice;

import com.todoapp.logintodoapp.login.dto.UserDTO;

public interface UserService {
    UserDTO createProfile(UserDTO userDTO);
    UserDTO getUserByUsername(String username);
}

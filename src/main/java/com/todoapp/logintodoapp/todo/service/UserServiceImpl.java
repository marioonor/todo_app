package com.todoapp.logintodoapp.todo.service;

import com.todoapp.logintodoapp.login.dto.UserDTO;
import com.todoapp.logintodoapp.login.loginentity.Users; 
import com.todoapp.logintodoapp.login.loginrepository.UserRepository; 
import com.todoapp.logintodoapp.login.loginservice.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createProfile(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + userDTO.getUsername() + "' is already taken.");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email '" + userDTO.getEmail() + "' is already registered.");
        }
        Users user = modelMapper.map(userDTO, Users.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); 
        Users savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return modelMapper.map(user, UserDTO.class);
    }
}

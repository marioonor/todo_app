package com.todoapp.logintodoapp.login.loginservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.login.jwtutil.JwtUtil;
import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.login.requests.AuthRequest;
import com.todoapp.logintodoapp.login.requests.RegisterRequest;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    public Users registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        return userRepository.save(user);
    }

    public String authenticate(AuthRequest request) {
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }
}

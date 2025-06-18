package com.todoapp.logintodoapp.login.logincontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.logintodoapp.login.jwtutil.JwtUtil; 
import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginservice.UserService;
import com.todoapp.logintodoapp.login.requests.AuthRequest;
import com.todoapp.logintodoapp.login.requests.AuthResponse;
import com.todoapp.logintodoapp.login.requests.RegisterRequest;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:4200")
    // @CrossOrigin(origins = "http://todoapp-front-end.s3-website-us-east-1.amazonaws.com")
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody RegisterRequest request) {
        Users registeredUser = userService.registerUser(request);
        return ResponseEntity.ok(registeredUser);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    // @CrossOrigin(origins = "http://todoapp-front-end.s3-website-us-east-1.amazonaws.com")
    
    // public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest request) {
    //     String token = userService.authenticate(request);
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        Users user = userService.authenticate(authRequest);
        String token = jwtUtil.generateToken(user);
        Date expirationDate = jwtUtil.extractExpirationDate(token);
        // return ResponseEntity.ok(new AuthResponse(token, expirationDate.getTime()));
        AuthResponse authResponse = new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                token,
                expirationDate.getTime());
        return ResponseEntity.ok(authResponse);
    }
}

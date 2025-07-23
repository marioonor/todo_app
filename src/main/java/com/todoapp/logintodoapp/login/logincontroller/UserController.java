package com.todoapp.logintodoapp.login.logincontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.logintodoapp.login.dto.UserDTO;
import com.todoapp.logintodoapp.login.jwtutil.JwtUtil; 
import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.login.loginservice.UserService;
import com.todoapp.logintodoapp.login.requests.AuthRequest;
import com.todoapp.logintodoapp.login.requests.AuthResponse;
import com.todoapp.logintodoapp.login.requests.RegisterRequest;
import com.todoapp.logintodoapp.todo.service.TokenBlacklistService;

import org.springframework.web.bind.annotation.RequestBody;
import org.modelmapper.ModelMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    private final TokenBlacklistService tokenBlacklistService;

    // @CrossOrigin(origins = "http://todoapp-front-end.s3-website-us-east-1.amazonaws.com")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public AuthResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("API /register is called for user: {}", registerRequest.getUsername());
        UserDTO userDTO = mapToProfileDTO(registerRequest); 
        UserDTO createdUser = userService.createProfile(userDTO); 
        log.info("Printing the created profile dto details {}", createdUser);
        return mapToProfileResponse(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        log.info("API /login is called for user: {}", authRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.warn("Login failed for user {}: Invalid credentials", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }

        Users authenticatedUser = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication")); 
        final String token = jwtUtil.generateToken(authenticatedUser);

        AuthResponse authResponse = AuthResponse.builder()
                                        .id(authenticatedUser.getId())
                                        .username(authenticatedUser.getUsername())
                                        .role(authenticatedUser.getRole())
                                        .token(token) 
                                        .build();
        log.info("User {} logged in successfully.", authRequest.getUsername());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        log.info("API /logout is called");
        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            if (jwtToken != null && !tokenBlacklistService.isTokenBlacklisted(jwtToken)) { 
                tokenBlacklistService.addTokenToBlacklist(jwtToken);
                log.info("Token blacklisted successfully for logout.");
                return ResponseEntity.ok("Logged out successfully.");
            }
        }
        log.warn("Logout attempt with no valid token or token already blacklisted/invalid.");
        return ResponseEntity.badRequest().body("Logout failed: No valid token provided or token already invalidated.");
    }
   
    private UserDTO mapToProfileDTO(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, UserDTO.class);
    }


    private AuthResponse mapToProfileResponse(UserDTO userDTO) {
        return modelMapper.map(userDTO, AuthResponse.class);
    }
}

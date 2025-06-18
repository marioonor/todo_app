package com.todoapp.logintodoapp.login.requests;

public class AuthResponse {
    
    private Long id;
    private String username;
    // private String email;
    // private String firstName;
    // private String lastName;
    // private String role;
    private final String token;
    private final String tokenType = "Bearer"; // Standard field, defaults to Bearer for JWT
    private final long expiresAt; // Expiration timestamp (milliseconds since epoch)

    // public AuthResponse(Long id, String username, String email, String firstName, String lastName, String role, String token, long expiresAt) {
    public AuthResponse(Long id, String username, String token, long expiresAt) {
        this.id = id;
        this.username = username;
        // this.email = email;
        // this.firstName = firstName;
        // this.lastName = lastName;
        // this.role = role;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    // public String getEmail() {
    //     return email;
    // }

    // public String getFirstName() {
    //     return firstName;
    // }

    // public String getLastName() {
    //     return lastName;
    // }

    // public String getRole() {
    //     return role;
    // }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresAt() {
        return expiresAt;
    }
}

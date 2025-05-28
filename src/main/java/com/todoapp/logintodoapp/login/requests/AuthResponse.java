package com.todoapp.logintodoapp.login.requests;

public class AuthResponse {
    
    private final String token;
    private final String tokenType = "Bearer"; // Standard field, defaults to Bearer for JWT
    private final long expiresAt; // Expiration timestamp (milliseconds since epoch)

    public AuthResponse(String token, long expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

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

package com.todoapp.logintodoapp.login.jwtutil;

import org.springframework.stereotype.Component;
import com.todoapp.logintodoapp.login.loginentity.Users;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Users users) {
        return Jwts.builder()
                .setSubject(users.getUsername())
                .claim("email", users.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token or parsing error extracting username", e);
        }
    }

    public String extractEmail(String token) {
        try {
            return getClaims(token).get("email", String.class);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token or parsing error extracting email", e);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpirationDate(token).before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    public Date extractExpirationDate(String token) {
        try {
            return getClaims(token).getExpiration();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token or parsing error extracting expiration date", e);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

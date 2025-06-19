package com.todoapp.logintodoapp.login.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.todoapp.logintodoapp.login.jwtutil.JwtUtil;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            logger.info("JWT Token from header: " + jwt);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("Extracted username: " + username);
            } catch (Exception e) {
                logger.warn("JWT token processing error during username extraction: " + e.getMessage(), e);
            }
        } else {
            logger.warn("Authorization header missing or does not start with Bearer for request URI: "
                    + request.getRequestURI());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Attempting to load UserDetails for username: " + username);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            boolean isValidToken = jwtUtil.validateToken(jwt, userDetails);
            logger.info("Token validation result for username " + username + ": " + isValidToken);

            if (isValidToken) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.info("Authentication set in SecurityContext for user: " + username);
            } else {
                logger.warn("Token validation failed for user: " + username + ". JWT: " + jwt);
            }
        } else {
            // Handle cases where username is null or user is already authenticated
            if (username == null && jwt != null) {
                // This case means username extraction failed but a token was present.
                logger.warn("Username could not be extracted from JWT, authentication context not set. JWT: " + jwt);
            } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
                // This case means user is already authenticated.
                logger.debug("User " + SecurityContextHolder.getContext().getAuthentication().getName()
                        + " already authenticated, skipping JWT processing for this filter for URI: "
                        + request.getRequestURI());
            } else if (jwt == null
                    && (request.getRequestURI() != null && !request.getRequestURI().startsWith("/auth/"))) {
                // No JWT and not an auth path.
                logger.debug("No JWT present in Authorization header for non-auth request URI: "
                        + request.getRequestURI() + ", proceeding without setting authentication context from JWT.");
            }
        }

        filterChain.doFilter(request, response);
    }
}

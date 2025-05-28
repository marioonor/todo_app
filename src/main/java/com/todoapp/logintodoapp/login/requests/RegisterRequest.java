package com.todoapp.logintodoapp.login.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class RegisterRequest {
    
    @NotBlank
    @Size(max = 20, message = "Username must be less than or equal to 20 characters")
    private String username;

    @Email
    @NotBlank
    @Size(max = 50, message = "Email must be less than or equal to 50 characters")
    private String email;

    @NotBlank
    @Size(min = 6, max = 70)
    private String password;

    @NotBlank
    @Size(max = 20, message = "First name must be less than or equal to 20 characters")
    private String firstName;

    @NotBlank
    @Size(max = 20, message = "Last name must be less than or equal to 20 characters")
    private String lastName;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username;}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}

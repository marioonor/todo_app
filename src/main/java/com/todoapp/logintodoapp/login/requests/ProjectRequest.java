package com.todoapp.logintodoapp.login.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectRequest {
    @NotBlank(message = "Project name cannot be blank")
    private String project;

    @NotNull(message = "User ID for the project cannot be null")
    private Long userId;
}


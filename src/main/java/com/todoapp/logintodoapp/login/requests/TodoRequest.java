package com.todoapp.logintodoapp.login.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TodoRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    private String description;
    @NotBlank(message = "Status cannot be blank")
    private String status;
    private String remarks;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalDate dueDate;
    private String priority;
    private Integer order;
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Project ID cannot be null")
    private Long projectId;
}


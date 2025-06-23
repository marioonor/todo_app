package com.todoapp.logintodoapp.login.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubtasksRequest {
    @NotBlank(message = "Subtask description cannot be blank")
    private String subtasks;
    private boolean completed = false; // Default to not completed
    @NotNull(message = "Todo ID for the subtask cannot be null")
    private Long todoId;
}


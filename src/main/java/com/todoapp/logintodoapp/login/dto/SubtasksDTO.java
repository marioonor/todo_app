package com.todoapp.logintodoapp.login.dto;

import lombok.Data; // and other lombok annotations

@Data // Add other Lombok annotations as needed
public class SubtasksDTO {
    private Long id;
    private String subtasks; 
    private boolean completed;
    private Long todoId;
    private String todoTitle;
}

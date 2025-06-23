package com.todoapp.logintodoapp.login.requests;

import lombok.Data; // Add other Lombok annotations

@Data // Add other Lombok annotations as needed
public class SubtasksResponse {
    private Long id;
    private String subtasks;
    private boolean completed;
    private Long todoId;
    private String todoTitle;
}

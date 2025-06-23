package com.todoapp.logintodoapp.login.requests;

import lombok.Data; // Add other Lombok annotations
import java.time.LocalDate;

@Data // Add other Lombok annotations as needed (Builder, NoArgsConstructor, AllArgsConstructor)
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String remarks;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalDate dueDate;
    private String priority;
    private Integer order;
    private Long userId;
    private String username;
    private Long projectId;
    private String projectName;
}


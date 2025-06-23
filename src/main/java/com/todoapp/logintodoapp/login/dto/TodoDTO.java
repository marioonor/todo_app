package com.todoapp.logintodoapp.login.dto;

import lombok.Data; // and other lombok annotations
import java.time.LocalDate; // Or LocalDateTime

@Data // Add other Lombok annotations as needed (Builder, NoArgsConstructor, AllArgsConstructor)
public class TodoDTO {
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
    private String username; // For response convenience
    private Long projectId;
    private String projectName; // For response convenience
}

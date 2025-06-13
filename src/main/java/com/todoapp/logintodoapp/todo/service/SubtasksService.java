package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.todo.todoentity.Subtasks;

@Service
public interface SubtasksService {
    
    Subtasks addSubtasks(Subtasks subtasks);

    List<Subtasks> fetchAllSubtasks();

    Subtasks updateSubtasks(Subtasks subtasks);

    String deleteSubtasks(Long id);

}

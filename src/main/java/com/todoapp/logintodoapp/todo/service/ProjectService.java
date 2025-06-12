package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.todo.todoentity.Project;

@Service
public interface ProjectService {
    
    Project addProject(Project project);

    List<Project> fetchAllProjects();

    Project updateProject(Project project);

    String deleteProject(Long id);

    // List<Project> findAll();
}

package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.login.dto.ProjectDTO;

@Service
public interface ProjectService {
    
    ProjectDTO addProject(ProjectDTO projectDTO);

    List<ProjectDTO> fetchAllProjects();

    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);

    void deleteProject(Long id);
}

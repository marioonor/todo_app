package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.todo.repository.ProjectRepository;
import com.todoapp.logintodoapp.todo.todoentity.Project;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Project addProject(Project project) {
        if (project.getUser() == null || project.getUser().getId() == null) {
            throw new IllegalArgumentException("Project must be associated with a valid User ID.");
        }
        Long userId = project.getUser().getId();
        Users managedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        project.setUser(managedUser);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> fetchAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project updateProject(Project project) {
        if (project.getId() == null) {
            throw new IllegalArgumentException("Project ID must not be null for an update.");
        }
        Project existingProject = projectRepository.findById(project.getId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + project.getId()));

        existingProject.setProject(project.getProject());

        if (project.getUser() != null && project.getUser().getId() != null &&
                !project.getUser().getId().equals(existingProject.getUser().getId())) {
            Long newUserId = project.getUser().getId();
            Users newManagedUser = userRepository.findById(newUserId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + newUserId));
            existingProject.setUser(newManagedUser);
        }
        return projectRepository.save(existingProject);
    }

    @Override
    public String deleteProject(Long id) {

        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
        projectRepository.delete(existingProject); 
        return "Project deleted successfully";
    }

}

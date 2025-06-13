package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.todo.repository.ProjectRepository;
import com.todoapp.logintodoapp.todo.todoentity.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> fetchAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    @Override
    public Project updateProject(Project project) {
        Project existingProject = projectRepository.findById(project.getId()).get();
        if (existingProject != null) {
            existingProject.setProject(project.getProject());
            // existingProject.setTask_id(project.getTask_id());
        }
        return projectRepository.save(project);
    }

    @Override
    public String deleteProject(Long id) {

        Project existingProject = projectRepository.findById(id).get();
        String message = null;
        if (existingProject != null) {
            projectRepository.deleteById(id);
            message = "Project deleted successfully";
        }
        return message;
    }

}

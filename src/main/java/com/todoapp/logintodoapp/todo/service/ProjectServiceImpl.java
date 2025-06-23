package com.todoapp.logintodoapp.todo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.login.dto.ProjectDTO;
import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.todo.repository.ProjectRepository;
import com.todoapp.logintodoapp.todo.todoentity.Project;
import org.springframework.security.core.Authentication;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Use constructor injection
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository,
            ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ProjectDTO addProject(ProjectDTO projectDTO) {
        logger.info("Received projectDTO in addProject: {}", projectDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            // This is the primary point of failure if the security context is not
            // populated.
            throw new IllegalStateException(
                    "User not authenticated or user details are unavailable. Cannot add project.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users projectOwner = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Authenticated user not found in database: " + userDetails.getUsername()));

        Project projectEntity = new Project();
        projectEntity.setProject(projectDTO.getProject()); // Set properties from DTO
        projectEntity.setUser(projectOwner); // Associate the managed user with the project entity
        Project savedProject = projectRepository.save(projectEntity);

        ProjectDTO savedProjectDTO = modelMapper.map(savedProject, ProjectDTO.class);

        savedProjectDTO.setUserId(savedProject.getUser().getId());
        savedProjectDTO.setUsername(savedProject.getUser().getUsername());
        return savedProjectDTO;
    }

    @Override
    @Transactional(readOnly = true) // readOnly = true is an optimization for read-only transactions
    public List<ProjectDTO> fetchAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            // Or throw an exception, or return empty list based on requirements
            throw new IllegalStateException("User not authenticated. Cannot fetch projects.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users authenticatedUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Authenticated user not found in database: " + userDetails.getUsername()));

        // Fetch projects only for the authenticated user
        List<Project> userProjects = projectRepository.findByUser_Id(authenticatedUser.getId()); // Use the corrected
                                                                                                 // method name

        return userProjects
                .stream()
                .map(project -> {
                    ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
                    if (project.getUser() != null) { // Ensure user is not null
                        dto.setUserId(project.getUser().getId()); // Use the corrected field name
                        dto.setUsername(project.getUser().getUsername());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        if (id == null) {
            throw new IllegalArgumentException("Project ID must not be null for an update.");
        }
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new IllegalStateException("User not authenticated. Cannot update project.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users authenticatedUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Authenticated user not found: " + userDetails.getUsername()));

        if (existingProject.getUser() == null || !existingProject.getUser().getId().equals(authenticatedUser.getId())) {
            // Add role-based checks if admins can update any project
            throw new SecurityException("User not authorized to update this project.");
        }
        existingProject.setProject(projectDTO.getProject()); // Assuming 'project' is the name field in DTO

        // Prevent changing the project owner via this method for non-admin users.
        // If the DTO contains a userId, ensure it's the same as the existing owner.
        if (projectDTO.getUserId() != null &&
                (existingProject.getUser() == null
                        || !projectDTO.getUserId().equals(existingProject.getUser().getId()))) {
            // If the provided userId is different from the current owner, disallow it for
            // now.
            throw new SecurityException("Cannot change project owner via this method.");
        }

        Project updatedProject = projectRepository.save(existingProject);
        ProjectDTO updatedProjectDTO = modelMapper.map(updatedProject, ProjectDTO.class);
        // Optionally populate username
        if (updatedProjectDTO.getUsername() == null && updatedProject.getUser() != null) {
            updatedProjectDTO.setUsername(updatedProject.getUser().getUsername());
        }
        if (updatedProjectDTO.getUserId() == null && updatedProject.getUser() != null) {
            updatedProjectDTO.setUserId(updatedProject.getUser().getId()); // Use the corrected field name
        }
        return updatedProjectDTO;
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        // Authorization check: Ensure the current user owns this project or has
        // permission
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id)); // Fetch first for
                                                                                                     // check

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new IllegalStateException("User not authenticated. Cannot delete project.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users authenticatedUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Authenticated user not found: " + userDetails.getUsername()));
        if (existingProject.getUser() == null || !existingProject.getUser().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("User not authorized to delete this project.");
        } else {
            projectRepository.delete(existingProject); // Delete only if authorized
        }
    }

}

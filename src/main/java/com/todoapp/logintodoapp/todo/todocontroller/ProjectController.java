package com.todoapp.logintodoapp.todo.todocontroller;

import com.todoapp.logintodoapp.login.dto.ProjectDTO;
import com.todoapp.logintodoapp.login.requests.ProjectRequest;
import com.todoapp.logintodoapp.login.requests.ProjectResponse;
import com.todoapp.logintodoapp.todo.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        log.info("API GET /api/projects called");
        List<ProjectDTO> projectDTOs = projectService.fetchAllProjects();
        log.info("Fetched {} projects from service", projectDTOs.size());
        return projectDTOs.stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{projectId}")
    public ProjectResponse getProjectById(@PathVariable Long projectId) {
        log.info("API GET /api/projects/{} called", projectId);
        ProjectDTO projectDTO = projectService.fetchAllProjects().stream() // Assuming fetchAllProjects and then filter, or add getProjectById to service
            .filter(p -> p.getId().equals(projectId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId)); // Replace with proper exception handling
        log.info("Fetched project details: {}", projectDTO);
        return mapToProjectResponse(projectDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        log.info("API POST /api/projects called with request: {}", projectRequest);
        ProjectDTO projectDTO = mapToProjectDTO(projectRequest);
        ProjectDTO createdProjectDTO = projectService.addProject(projectDTO);
        log.info("Created project: {}", createdProjectDTO);
        return mapToProjectResponse(createdProjectDTO);
    }

    @PutMapping("/{projectId}")
    public ProjectResponse updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectRequest projectRequest) {
        log.info("API PUT /api/projects/{} called with request: {}", projectId, projectRequest);
        ProjectDTO projectDTO = mapToProjectDTO(projectRequest);
        ProjectDTO updatedProjectDTO = projectService.updateProject(projectId, projectDTO);
        log.info("Updated project: {}", updatedProjectDTO);
        return mapToProjectResponse(updatedProjectDTO);
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long projectId) {
        log.info("API DELETE /api/projects/{} called", projectId);
        projectService.deleteProject(projectId);
        log.info("Deleted project with id: {}", projectId);
    }

    private ProjectDTO mapToProjectDTO(ProjectRequest projectRequest) {
        ProjectDTO dto = modelMapper.map(projectRequest, ProjectDTO.class);
        return dto;
    }

    private ProjectResponse mapToProjectResponse(ProjectDTO projectDTO) {
        ProjectResponse response = modelMapper.map(projectDTO, ProjectResponse.class);
        return response;
    }
}

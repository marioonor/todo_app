package com.todoapp.logintodoapp.todo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todoapp.logintodoapp.login.requests.TodoResponse;
import com.todoapp.logintodoapp.login.requests.TodoRequest;
import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.todo.repository.ProjectRepository;
import com.todoapp.logintodoapp.todo.repository.SubtasksRepository;
import com.todoapp.logintodoapp.todo.repository.TodoRepository;
import com.todoapp.logintodoapp.todo.todoentity.Project;
import com.todoapp.logintodoapp.todo.todoentity.Subtasks;
import com.todoapp.logintodoapp.todo.todoentity.Todo;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SubtasksRepository subtasksRepository;
    
    @Autowired
    private ModelMapper modelMapper; // Ensure ModelMapper is injected

    @Override
    @Transactional
    public Todo addTodo(TodoRequest todoRequest) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + username));

        // Find the project entity
        Project project = projectRepository.findById(todoRequest.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + todoRequest.getProjectId()));

        // Map DTO to entity and set associations
        Todo todoEntity = modelMapper.map(todoRequest, Todo.class);
        todoEntity.setUser(currentUser);
        todoEntity.setProject(project);

        return todoRepository.save(todoEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> fetchAllTodos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new IllegalStateException("User not authenticated. Cannot fetch todos.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users authenticatedUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Authenticated user not found in database: " + userDetails.getUsername()));

        List<Todo> userTodos = todoRepository.findByUser_Id(authenticatedUser.getId());

        return userTodos.stream()
                .map(todo -> {
                    TodoResponse dto = modelMapper.map(todo, TodoResponse.class);
                    if (todo.getUser() != null) {
                        dto.setUserId(todo.getUser().getId());
                        dto.setUsername(todo.getUser().getUsername());
                    }
                    if (todo.getProject() != null) {
                        dto.setProjectId(todo.getProject().getId());
                        dto.setProjectName(todo.getProject().getProject());
                    }
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Map<String, String> deleteTodo(Long id) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Todo not found with ID: " + id));

        // Find and delete associated subtasks
        List<Subtasks> associatedSubtasks = subtasksRepository.findByTodoId(id);
        if (!associatedSubtasks.isEmpty()) {
            subtasksRepository.deleteAll(associatedSubtasks);
        }

        todoRepository.delete(existingTodo);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo deleted successfully! ID: " + id);
        return response;
    }

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Todo> getTodos() {
        // Get the authentication object for the current user from the security context.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            // This case should ideally be handled by Spring Security, but as a safeguard:
            throw new IllegalStateException("User not authenticated. Cannot fetch todos.");
        }

        // Extract user details and find the corresponding user entity from the database.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users authenticatedUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Authenticated user not found in database: " + userDetails.getUsername()));

        // Fetch and return the list of todos associated with the authenticated user's ID.
        return todoRepository.findByUser_Id(authenticatedUser.getId());
    }

    @Override
    @Transactional
    public Todo updateTodo(Long id, TodoRequest todoRequest) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with ID: " + id));

        // Authorization check: Ensure the current user owns this todo
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Make sure user owns this Todo, AND has access to the project.
        // If the user does NOT own the todo OR the user does NOT own the project, then throw a SecurityException.
        if (!existingTodo.getUser().getUsername().equals(userDetails.getUsername()) || // User does not own the todo
            !existingTodo.getProject().getUser().getUsername().equals(userDetails.getUsername())) { // User does not own the project
            throw new SecurityException("User not authorized to update this todo.");
        }

        // Update fields from the provided details
        existingTodo.setTitle(todoRequest.getTitle());
        existingTodo.setDescription(todoRequest.getDescription());
        existingTodo.setStatus(todoRequest.getStatus());
        existingTodo.setRemarks(todoRequest.getRemarks());
        existingTodo.setDateStart(
            todoRequest.getDateStart() != null ? todoRequest.getDateStart().toString() : null
        );
        existingTodo.setDateEnd(
            todoRequest.getDateEnd() != null ? todoRequest.getDateEnd().toString() : null
        );
        existingTodo.setDueDate(
            todoRequest.getDueDate() != null ? todoRequest.getDueDate().toString() : null
        );
        existingTodo.setPriority(todoRequest.getPriority());
        existingTodo.setOrder(todoRequest.getOrder());

        // Handle project change if a new project ID is provided and is different.
        Long newProjectId = todoRequest.getProjectId();
        if (newProjectId != null && !newProjectId.equals(existingTodo.getProject().getId())) {
            Project newProject = projectRepository.findById(newProjectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + newProjectId));
            // Additional authorization check might be needed here to ensure user can access the new project.
            existingTodo.setProject(newProject);
        }

        return todoRepository.save(existingTodo);
    }
}

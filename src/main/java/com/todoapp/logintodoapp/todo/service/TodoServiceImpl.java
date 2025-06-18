package com.todoapp.logintodoapp.todo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.todo.repository.ProjectRepository;
import com.todoapp.logintodoapp.todo.repository.SubtasksRepository;
import com.todoapp.logintodoapp.todo.repository.TodoRepository;
import com.todoapp.logintodoapp.todo.todoentity.Project;
import com.todoapp.logintodoapp.todo.todoentity.Subtasks;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

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

    @Override
    public Todo addTodo(Todo todo) {
        if (todo.getUser() == null || todo.getUser().getId() == null || todo.getProject() == null
                || todo.getProject().getId() == null) {
            throw new IllegalArgumentException("Todo must be associated with a valid User ID and Project ID.");
        }
        Long userId = todo.getUser().getId();
        Long projectId = todo.getProject().getId();
        Users managedUsers = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Project managedProjects = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        todo.setUser(managedUsers);
        todo.setProject(managedProjects);
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> fetchAllTodos() {
        return (List<Todo>) todoRepository.findAll();
    }

    @Override
    public Todo updateTodo(Todo todo) {
        if (todo.getId() == null) {
            throw new IllegalArgumentException("Todo ID must not be null for an update.");
        }
        Todo existingTodo = todoRepository.findById(todo.getId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Todo not found with ID: " + todo.getId()));

        existingTodo.setTitle(todo.getTitle());
        existingTodo.setDescription(todo.getDescription());
        existingTodo.setStatus(todo.getStatus());
        existingTodo.setRemarks(todo.getRemarks());
        existingTodo.setDateStart(todo.getDateStart());
        existingTodo.setDateEnd(todo.getDateEnd());
        existingTodo.setDueDate(todo.getDueDate());
        existingTodo.setPriority(todo.getPriority());
        existingTodo.setOrder(todo.getOrder()); 

        // Handle user update
        if (todo.getUser() != null && todo.getUser().getId() != null) {
            Users managedUser = userRepository.findById(todo.getUser().getId())
                    .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                            "User not found with ID: " + todo.getUser().getId()));
            existingTodo.setUser(managedUser);
        }

        // Handle project update
        if (todo.getProject() != null && todo.getProject().getId() != null) {
            Project managedProject = projectRepository.findById(todo.getProject().getId())
                    .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                            "Project not found with ID: " + todo.getProject().getId()));
            existingTodo.setProject(managedProject);
        }
        return todoRepository.save(existingTodo);
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
}

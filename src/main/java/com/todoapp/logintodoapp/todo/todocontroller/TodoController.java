package com.todoapp.logintodoapp.todo.todocontroller;

import com.todoapp.logintodoapp.login.requests.TodoRequest;
import com.todoapp.logintodoapp.todo.service.TodoService;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos") // Assuming this is the correct base path for todos
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Map<String, Object>> getAllTodos() {
        log.info("API GET /api/todos called");
        // Note: Ensure your service method is @Transactional to avoid LazyInitializationException
        List<Todo> todos = todoService.getTodos();
        return todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTodoById(@PathVariable Long id) {
        log.info("API GET /api/todos/{} called", id);
        return todoService.getTodoById(id) // Now returns Optional<Todo>
                .map(this::convertToResponse) // Map Optional<Todo> to Optional<Map<String, Object>>
                .map(ResponseEntity::ok) // Map Optional<Map<String, Object>> to Optional<ResponseEntity<Map<String, Object>>>
                .orElse(ResponseEntity.notFound().build()); // If Optional is empty, return 404
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addTodo(@Valid @RequestBody TodoRequest todoRequest) {
        log.info("TodoRequest.title before mapping: '{}'", todoRequest.getTitle()); // NEW DEBUG LOG
        log.info("Received request to add todo: {}", todoRequest); // Log full DTO for inspection

        // Explicit check for title.
        if (todoRequest.getTitle() == null || todoRequest.getTitle().trim().isEmpty()) {
            log.error("Validation failed: Todo title is null or empty. Received: '{}'", todoRequest.getTitle());
            return ResponseEntity.badRequest().build();
        }

        // The service layer now handles entity creation and association
        Todo savedTodo = todoService.addTodo(todoRequest);
        return new ResponseEntity<>(convertToResponse(savedTodo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequest todoRequest) {
        log.info("API PUT /api/todos/{} called", id);
        
        // The service layer now handles the update logic
        Todo updatedTodo = todoService.updateTodo(id, todoRequest);
        return ResponseEntity.ok(convertToResponse(updatedTodo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTodo(@PathVariable Long id) {
        log.info("API DELETE /api/todos/{} called", id);
        todoService.deleteTodo(id);
        return ResponseEntity.ok(Map.of("message", "Todo with ID " + id + " deleted successfully."));
    }

    /**
     * Maps a Todo entity to a Map structure suitable for JSON response.
     * This prevents LazyInitializationException and potential circular serialization issues.
     */
    private Map<String, Object> convertToResponse(Todo todo) {
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", todo.getId());
        response.put("title", todo.getTitle());
        response.put("description", todo.getDescription());
        response.put("status", todo.getStatus());
        response.put("remarks", todo.getRemarks() != null ? todo.getRemarks() : "");
        response.put("dateStart", todo.getDateStart());
        response.put("dateEnd", todo.getDateEnd());
        response.put("dueDate", todo.getDueDate());
        response.put("priority", todo.getPriority());
        response.put("order", todo.getOrder() != null ? todo.getOrder() : 0);
        response.put("user", Map.of(
            "id", todo.getUser().getId(),
            "username", todo.getUser().getUsername(),
            "role", todo.getUser().getRole()
        ));
        response.put("project", Map.of(
            "id", todo.getProject().getId(),
            "project", todo.getProject().getProject()
        ));
        response.put("userId", todo.getUser().getId());
        response.put("projectId", todo.getProject().getId());
        return response;
    }
}
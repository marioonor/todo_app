package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.todo.repository.SubtasksRepository;
import com.todoapp.logintodoapp.todo.repository.TodoRepository;
import com.todoapp.logintodoapp.todo.todoentity.Subtasks;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SubtasksServiceImpl implements SubtasksService {

    @Autowired
    private SubtasksRepository subtasksRepository;

    @Autowired
    TodoRepository todoRepository;

    @Override
    public Subtasks addSubtasks(Subtasks subtasks) {
        // Ensure the associated Todo entity is managed
        if (subtasks.getTodo() == null || subtasks.getTodo().getId() == null) {
            // This condition means the client did not provide a valid Todo reference.
            // Since Subtasks.todo is @ManyToOne(optional = false), this is an invalid
            // state.
            throw new IllegalArgumentException(
                    "Subtask must be associated with a valid Todo ID. Received Todo: " + subtasks.getTodo());
        }

        Long todoId = subtasks.getTodo().getId(); // Get ID from the (potentially transient) Todo from request
        Todo managedTodo = todoRepository.findById(todoId) // Fetch managed instance
                .orElseThrow(() -> new EntityNotFoundException("Parent Todo not found with ID: " + todoId));
        subtasks.setTodo(managedTodo);
        return subtasksRepository.save(subtasks);
    }

    @Override
    public List<Subtasks> fetchAllSubtasks() {
        return (List<Subtasks>) subtasksRepository.findAll();
    }

    @Override
    public Subtasks updateSubtasks(Subtasks subtaskDetails) {
        Subtasks existingSubtasks = subtasksRepository.findById(subtaskDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("Subtask not found with ID: " + subtaskDetails.getId()));

        existingSubtasks.setSubtasks(subtaskDetails.getSubtasks());
        existingSubtasks.setCompleted(subtaskDetails.isCompleted());

        // Handle updates to the parent Todo association if necessary
        if (subtaskDetails.getTodo() != null && subtaskDetails.getTodo().getId() != null) {
            // Check if the parent Todo ID is different from the existing one
            if (existingSubtasks.getTodo() == null
                    || !existingSubtasks.getTodo().getId().equals(subtaskDetails.getTodo().getId())) {
                Todo managedTodo = todoRepository.findById(subtaskDetails.getTodo().getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Parent Todo not found with ID: " + subtaskDetails.getTodo().getId()));
                existingSubtasks.setTodo(managedTodo);
            }
        } else {

        }
        return subtasksRepository.save(existingSubtasks);
    }

    @Override
    public String deleteSubtasks(Long id) {
        Subtasks existingSubtasks = subtasksRepository.findById(id).get();
        String message = null;
        if (existingSubtasks != null) {
            subtasksRepository.deleteById(id);
            message = "Subtasks deleted successfully";
        }
        return message;
    }

}

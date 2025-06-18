package com.todoapp.logintodoapp.todo.todocontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.logintodoapp.todo.service.SubtasksService;
import com.todoapp.logintodoapp.todo.todoentity.Subtasks;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/subtasks")
public class SubtasksController {

    @Autowired
    private SubtasksService subtasksService;

    @PostMapping
    public ResponseEntity<Subtasks> addSubtasks(@RequestBody Subtasks subtasks) {
        Subtasks createdSubtask = subtasksService.addSubtasks(subtasks);
        return new ResponseEntity<>(createdSubtask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Subtasks>> fetchAllSubtasks() {
        return ResponseEntity.ok(subtasksService.fetchAllSubtasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subtasks> updateSubtasks(@PathVariable("id") Long id, @RequestBody Subtasks subtasks) {
        subtasks.setId(id);
        Subtasks updatedSubtask = subtasksService.updateSubtasks(subtasks);
        return ResponseEntity.ok(updatedSubtask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubtasks(@PathVariable("id") Long id) {
        subtasksService.deleteSubtasks(id);
        Map<String, String> response = Map.of("message", "Subtask deleted successfully! ID: " + id);
        return ResponseEntity.ok(response);
    }

}

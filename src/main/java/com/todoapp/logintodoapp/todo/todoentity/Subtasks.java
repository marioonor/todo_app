package com.todoapp.logintodoapp.todo.todoentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subtasks")
public class Subtasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String subtasks;

    private boolean completed;

     @ManyToOne(optional = false) // Indicates 'todo' field must be non-null for a persistent Subtask
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(String subtasks) {
        this.subtasks = subtasks;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

}

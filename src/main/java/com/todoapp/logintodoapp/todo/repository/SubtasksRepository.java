package com.todoapp.logintodoapp.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.logintodoapp.todo.todoentity.Subtasks;

public interface SubtasksRepository extends JpaRepository<Subtasks, Long> {
    
}

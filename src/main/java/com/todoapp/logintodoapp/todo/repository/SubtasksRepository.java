package com.todoapp.logintodoapp.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.logintodoapp.todo.todoentity.Subtasks;

public interface SubtasksRepository extends JpaRepository<Subtasks, Long> {
    
    List<Subtasks> findByTodoId(Long todoId);
    void deleteByTodoId(Long todoId);
}

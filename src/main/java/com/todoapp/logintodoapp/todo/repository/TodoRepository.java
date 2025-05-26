package com.todoapp.logintodoapp.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.logintodoapp.todo.todoentity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    
}

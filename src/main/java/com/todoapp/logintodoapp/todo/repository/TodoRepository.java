package com.todoapp.logintodoapp.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.logintodoapp.todo.todoentity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser_Id(Long userId);
}

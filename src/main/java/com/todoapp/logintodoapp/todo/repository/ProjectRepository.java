package com.todoapp.logintodoapp.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.logintodoapp.todo.todoentity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    
}

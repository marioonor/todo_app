package com.todoapp.logintodoapp.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.logintodoapp.todo.todoentity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser_Id(Long userId);
}

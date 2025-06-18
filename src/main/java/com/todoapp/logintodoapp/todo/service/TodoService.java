package com.todoapp.logintodoapp.todo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

@Service
public interface TodoService {

    Todo addTodo(Todo todo);

    List<Todo> fetchAllTodos();

    Todo updateTodo(Todo todo);

    Map<String, String> deleteTodo(Long id);

    List<Todo> findAll();
}

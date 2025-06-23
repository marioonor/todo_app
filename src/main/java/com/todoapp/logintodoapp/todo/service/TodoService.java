package com.todoapp.logintodoapp.todo.service;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.login.requests.TodoRequest;
import com.todoapp.logintodoapp.login.requests.TodoResponse;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

@Service
public interface TodoService {

    Todo addTodo(TodoRequest todoRequest);

    List<TodoResponse> fetchAllTodos();

    Map<String, String> deleteTodo(Long id);

    List<Todo> findAll();

    List<Todo> getTodos();

    Optional<Todo> getTodoById(Long id);

    Todo updateTodo(Long id, TodoRequest todoRequest);
}

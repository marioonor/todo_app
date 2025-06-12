package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.todo.repository.TodoRepository;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo); 
    }

    @Override
    public List<Todo> fetchAllTodos() {
        return (List<Todo>) todoRepository.findAll();
    }

    @Override
    public Todo updateTodo(Todo todo) {
        Todo existingTodo = todoRepository.findById(todo.getId()).get();
        if (existingTodo != null) {
            existingTodo.setTitle(todo.getTitle());
            existingTodo.setDescription(todo.getDescription());
            existingTodo.setStatus(todo.getStatus());
            existingTodo.setRemarks(todo.getRemarks());
            existingTodo.setDateStart(todo.getDateStart());
            existingTodo.setDateEnd(todo.getDateEnd()); 
            existingTodo.setDueDate(todo.getDueDate());
            existingTodo.setPriority(todo.getPriority());
        }
        return todoRepository.save(todo);
    }

    @Override
    public String deleteTodo(Long id) {
        Todo existingTodo = todoRepository.findById(id).get();
        String message = null;
        if (existingTodo != null) {
            todoRepository.deleteById(id);
            message = "Todo deleted successfully! " + id;
        }
        
        return message;
    }

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }
}

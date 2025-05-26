package com.todoapp.logintodoapp.todo.todocontroller;

import com.todoapp.logintodoapp.todo.service.TodoService;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

import org.springframework.ui.Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class TodoController {

    @Autowired
    private TodoService todoService;
    
    // @PostMapping("addtodo")
    // public ResponseEntity<Todo> addTodo(Todo todo) {
    //     return ResponseEntity.ok(todoService.addTodo(todo));  
    // }

    @PostMapping("addtodo")
    public String addTodo(Todo todo, Model model) {
        todoService.addTodo(todo);
        model.addAttribute("todo", todoService.fetchAllTodos());
        return "redirect:/todolist"; 
    }

    @GetMapping("viewtodo")
    public ResponseEntity<List<Todo>> viewTodo() {
        return ResponseEntity.ok(todoService.fetchAllTodos());
    }

    @PostMapping("updatetodo/{id}")
    public String updateTodo(@PathVariable("id") Long id, Todo todo, Model model) {
        // return ResponseEntity.ok(todoService.updateTodo(todo));
        todoService.updateTodo(todo);
        model.addAttribute("todoList", todoService.fetchAllTodos());
        return "redirect:/todolist";
    }

    @PostMapping("deletetodo/{id}")
    public String deleteTodo(@PathVariable("id") Long id, Model model) {
        // return ResponseEntity.ok(todoService.deleteTodo(id));
        todoService.deleteTodo(id);
        model.addAttribute("todoList", todoService.fetchAllTodos());
        return "redirect:/todolist";
    }
    
     
}

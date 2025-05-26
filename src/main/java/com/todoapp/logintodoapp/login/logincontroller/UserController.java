package com.todoapp.logintodoapp.login.logincontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.todoapp.logintodoapp.login.loginentity.Users;
import com.todoapp.logintodoapp.login.loginrepository.UserRepository;
import com.todoapp.logintodoapp.todo.service.TodoService;
import com.todoapp.logintodoapp.todo.todoentity.Todo;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Principal principal) {
        if (principal != null) {
            String redirectUrl = request.getContextPath() + "/todolist";
            return "redirect:" + redirectUrl;
        }
        return "login";
    }

    @GetMapping("/register")
    public String signUp(Model model) {
        model.addAttribute("users", new Users());
        return "signup";
    }

    @PostMapping("/process_register")
    public String processRegistration(Users users) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);
        userRepository.save(users);
        return "register_success";
    }

    @Autowired
    private TodoService todoService;

    @GetMapping("/todolist")
    public String todoList(Model model) {
        List<Todo> todoList = todoService.findAll();
        model.addAttribute("todoList", todoList);
        return "todolist";
    }



}

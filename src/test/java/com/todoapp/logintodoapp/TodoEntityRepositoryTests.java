package com.todoapp.logintodoapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.todoapp.logintodoapp.todo.repository.TodoRepository;
import com.todoapp.logintodoapp.todo.todoentity.Todo;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class TodoEntityRepositoryTests {
    
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateTodo() {

        Todo todo = new Todo();
        todo.setTitle("test todo");
        todo.setDescription("test description");
        todo.setStatus("test status");
        todo.setRemarks("test remarks");
        todo.setDateStart("2023-10-01");
        todo.setDateEnd("2023-10-31");
        
        Todo savedTodo = todoRepository.save(todo);
        Todo foundTodo = entityManager.find(Todo.class, savedTodo.getId());
        assertThat(foundTodo.getTitle()).isEqualTo(todo.getTitle());
    }

    @Test
    public void testUpdateTodo() {
        Todo todo = new Todo();
        todo.setTitle("update todo");
        todo.setDescription("This is an update description");
        todo.setStatus("update status");
        todo.setRemarks("update remarks");
        todo.setDateStart("2023-11-01");
        todo.setDateEnd("2023-11-30");

        Todo savedTodo = todoRepository.save(todo);
        savedTodo.setTitle("updated title");
        
        Todo updatedTodo = todoRepository.save(savedTodo);
        assertThat(updatedTodo.getTitle()).isEqualTo("updated title");
    }

    @Test
    public void testDeleteTodoById() {
        // First, create and save a Todo
        Todo todo = new Todo();
        todo.setTitle("delete todo");
        todo.setDescription("delete description");
        todo.setStatus("delete status");
        todo.setRemarks("delete remarks");
        todo.setDateStart("2023-12-01");
        todo.setDateEnd("2023-12-31");

        Todo savedTodo = todoRepository.save(todo);

        todoRepository.deleteById(savedTodo.getId());

        boolean exists = todoRepository.findById(savedTodo.getId()).isPresent();
        assertThat(exists).isFalse();
    }

    
}

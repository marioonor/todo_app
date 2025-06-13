package com.todoapp.logintodoapp.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoapp.logintodoapp.todo.repository.SubtasksRepository;
import com.todoapp.logintodoapp.todo.todoentity.Subtasks;

@Service
public class SubtasksServiceImpl implements SubtasksService {

    @Autowired
    private SubtasksRepository subtasksRepository;

    @Override
    public Subtasks addSubtasks(Subtasks subtasks) {
        return subtasksRepository.save(subtasks);
    }

    @Override
    public List<Subtasks> fetchAllSubtasks() {
        return (List<Subtasks>) subtasksRepository.findAll();
    }

    @Override
    public Subtasks updateSubtasks(Subtasks subtasks) {
        Subtasks existingSubtasks = subtasksRepository.findById(subtasks.getId()).get();
        if (existingSubtasks != null) {
            existingSubtasks.setSubtasks(subtasks.getSubtasks());
        }
        return subtasksRepository.save(subtasks);
    }

    @Override
    public String deleteSubtasks(Long id) {
        Subtasks existingSubtasks = subtasksRepository.findById(id).get();
        String message = null;
        if (existingSubtasks != null) {
            subtasksRepository.deleteById(id);
            message = "Subtasks deleted successfully";
        }
        return message;
    }
    
}

package com.taskmanager.service.imp;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private TaskRepository repository;

    @Override
    public Optional<List<Task>> findAll(Long userTaskId) {
        Optional<List<Task>> tasks = repository.findByUserId(userTaskId);
        return Optional.ofNullable(tasks.orElseThrow(RuntimeException::new));
    }

    @Override
    public Task findById(Long id,Long userId) {
        return repository.findByIdAndUserId(id,userId).orElseThrow(() ->
                new RuntimeException("Tarefa não encontrada com ID: " + id));
    }

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public void delete(Task task) {
        repository.delete(task);
    }

    @Override
    public void update(Task task) {
        try {
            repository.save(task);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a tarefa");
        }
    }
}


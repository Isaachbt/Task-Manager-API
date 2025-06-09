package com.taskmanager.service;

import com.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Optional<List<Task>> findAll(Long userTaskId);
    Task findById(Long id,Long userId);
    Task save(Task task);
    void delete(Task task);
    void update(Task task);

}

package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.dto.UpdateTaskDTO;
import com.taskmanager.sercurit.AuthFacade;
import com.taskmanager.service.imp.AuthService;
import com.taskmanager.service.imp.TaskServiceImp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskServiceImp service;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthFacade authFacade;

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody TaskDTO dto) {
        try {
            Long currentUserId = authFacade.getCurrentUserId();

            if (!currentUserId.equals(dto.userId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuário não autorizado a realizar essa ação.");
            }

            if (!authService.userExists(dto.userId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário inválido, não encontrado.");
            }

            var task = new Task();
            BeanUtils.copyProperties(dto, task);
            task.setDataCriacao(LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.save(task));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou sessão expirada");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody UpdateTaskDTO dto) {
        try {
            Long currentUserId = authFacade.getCurrentUserId();

            if (!currentUserId.equals(dto.userId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuário não autorizado a realizar essa ação.");
            }

            if (!authService.userExists(dto.userId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário inválido, não encontrado.");
            }

            var task = service.findById(dto.id(),currentUserId);
            BeanUtils.copyProperties(dto, task);
            //task.setDataCriacao(LocalDateTime.now());

            return ResponseEntity.ok(service.save(task));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou sessão expirada");
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> findAll() {
        Long currentUserId = authFacade.getCurrentUserId();
        return ResponseEntity.ok(service.findAll(currentUserId)
                .orElseThrow(() -> new RuntimeException("Nenhuma tarefa encontrada")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        try {
            Long currentUserId = authFacade.getCurrentUserId();
            return ResponseEntity.ok(service.findById(id, currentUserId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            Long currentUserId = authFacade.getCurrentUserId();
            Task task = service.findById(id, currentUserId);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build();
            }
            service.delete(task);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}


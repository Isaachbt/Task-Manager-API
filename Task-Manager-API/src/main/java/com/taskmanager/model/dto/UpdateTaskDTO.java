package com.taskmanager.model.dto;

import com.taskmanager.model.Status;

import java.time.LocalDateTime;

public record UpdateTaskDTO(long id, String titulo, String descricao, Status status, LocalDateTime dataConclucao, Long userId) {
}

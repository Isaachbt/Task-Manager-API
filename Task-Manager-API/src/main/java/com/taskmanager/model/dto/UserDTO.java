package com.taskmanager.model.dto;

import com.taskmanager.model.Role;

public record UserDTO(Long id, String nome, String email, String senha, Role role) {
}

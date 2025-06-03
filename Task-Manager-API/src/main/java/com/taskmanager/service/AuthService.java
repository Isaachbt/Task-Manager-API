package com.taskmanager.service;

import com.taskmanager.model.User;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public void login() {

    }

    public String cadastro(UserDTO dtoUser){
        User userModel = new User();
        BeanUtils.copyProperties(dtoUser, userModel);

        if (userModel.getEmail() != null){
            userRepository.save(userModel);
            return "Cadastro realizado com sucesso!";
        }
        return "Erro ao cadastrar usuário!";
    }
}

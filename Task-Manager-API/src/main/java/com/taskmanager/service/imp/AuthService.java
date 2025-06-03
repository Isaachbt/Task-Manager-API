package com.taskmanager.service.imp;

import com.taskmanager.model.User;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String cadastro(User user){

        if (user.getEmail() != null){
            userRepository.save(user);
            return "Cadastro realizado com sucesso!";
        }
        return "Erro ao cadastrar usuário!";
    }

    public Optional<User> findByLogin(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}

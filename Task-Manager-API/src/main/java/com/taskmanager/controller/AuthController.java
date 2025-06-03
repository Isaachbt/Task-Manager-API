package com.taskmanager.controller;

import com.taskmanager.model.User;
import com.taskmanager.model.dto.LoginDTO;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.service.AuthenticationService;
import com.taskmanager.service.imp.AuthService;
import com.taskmanager.service.imp.AuthenticationServiceImp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationServiceImp authenticationServiceImp;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager manager;


    @PostMapping
    public ResponseEntity<String> cadastro(@RequestBody UserDTO dtoUser){

        Optional<User> loginExist = authService.findByLogin(dtoUser.email());

        if (loginExist.isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado!");
        }

        var passWord = passwordEncoder.encode(dtoUser.senha());
        var user = new User();
        BeanUtils.copyProperties(dtoUser, user);
        user.setSenha(passWord);

        try{
            authService.cadastro(user);
            return ResponseEntity.ok("Cadastro realizado com sucesso!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){
        User searchUser = authService.findByLogin(dto.email()).orElseThrow(RuntimeException::new);
        if (!passwordEncoder.matches(dto.senha(), searchUser.getSenha())){
            throw new RuntimeException("Senha incorreta!");
        }
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        manager.authenticate(authenticationToken);

        return ResponseEntity.ok(authenticationServiceImp.login(dto));
    }
}

package com.taskmanager.service.imp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.taskmanager.model.User;
import com.taskmanager.model.dto.LoginDTO;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.AuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class AuthenticationServiceImp implements AuthenticationService {

    @Autowired
    private UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = repository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }

        return user.get();
    }
    @Override
    public String login(LoginDTO dto) {
        return obterToken(dto);
    }

    @Override
    public String obterToken(LoginDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto,user);
        return gerarTokenJwt(user);
    }

    @Override
    public String validToken(String token) {
        try{

            Algorithm algorithm = Algorithm.HMAC256("my-secret");
            return JWT.require(algorithm)
                    .withIssuer("task")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return "";
        }
    }

    @Override
    public String gerarTokenJwt(User user) {
        try{

            Algorithm algorithm = Algorithm.HMAC256("my-secret");
            return JWT.create()
                    .withIssuer("task")
                    .withSubject(user.getEmail())
                    .withExpiresAt(gerarDateExpiracao())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("Erro ao tentar gerar o token");
        }
    }

    private Instant gerarDateExpiracao(){
        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String getLoginFromExpiredToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");
            return JWT.require(algorithm)
                    .withIssuer("task")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            return JWT.decode(token).getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

}

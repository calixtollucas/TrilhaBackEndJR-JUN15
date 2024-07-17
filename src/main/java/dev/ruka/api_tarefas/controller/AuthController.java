package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.model.user.UserLoginResponseDTO;
import dev.ruka.api_tarefas.model.user.UserRequestPayload;
import dev.ruka.api_tarefas.services.AuthUserService;
import dev.ruka.api_tarefas.services.JWTTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestPayload payload){
        //persiste no banco
        authUserService.save(payload);
        return ResponseEntity.ok("Logado com Sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@RequestBody @Valid UserRequestPayload payload){
        //cria um token de autenticação utilizando os dado recebidos
        UsernamePasswordAuthenticationToken userLogin = new UsernamePasswordAuthenticationToken(
                payload.username(),
                payload.password());

        //tenta autenticar o usuário, se for possível ser autenticado, gera o token, se não, lança exceção
        try{
            authenticationManager.authenticate(userLogin);
            //gera o token
            String jwtToken = tokenService.generateToken((User) userLogin.getPrincipal());
            //retorna o token
            return ResponseEntity.ok(new UserLoginResponseDTO(jwtToken));
        } catch (AuthenticationException e){
            throw new BusinessException(e.getClass().getName(), e.getMessage());
        }
    }
}

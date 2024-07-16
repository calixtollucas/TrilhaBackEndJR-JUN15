package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.model.user.UserRequestPayload;
import dev.ruka.api_tarefas.services.AuthUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestPayload payload){
        //persiste no banco
        authUserService.save(payload);
        return ResponseEntity.ok("Logado com Sucesso");
    }
}

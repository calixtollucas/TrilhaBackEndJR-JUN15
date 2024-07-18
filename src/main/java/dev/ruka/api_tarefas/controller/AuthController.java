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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    PasswordEncoder passwordEncoder;

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
        //procura o usuário
        User user = authUserService.findByUsername(payload.username());
        if(user == null) throw new BusinessException(AuthenticationException.class.getName(), "the user does not exists");
        if(passwordEncoder.matches(payload.password(), user.getPassword())){
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new UserLoginResponseDTO(token));
        } else {
            throw new BusinessException(AuthenticationException.class.getName(), "incorrect password");
        }
    }
}

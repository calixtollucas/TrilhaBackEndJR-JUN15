package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.user.UserResponseDTO;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    AreaService areaService;

    @GetMapping("/areas")
    public ResponseEntity<Set<Area>> getAreasFromUser(@RequestHeader(name = "Authorization") String token){
        String rawToken = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(rawToken);

        Set<Area> areasResponse = areaService.getAllAreasFromUser(userId);
        if(areasResponse == null){
            throw new BusinessException(AuthenticationException.class.getName(), "the user does not have areas");
        }
        //TODO retornar um UserResponseDTO
        //TODO testar
        return ResponseEntity.ok(areasResponse);
    }
}

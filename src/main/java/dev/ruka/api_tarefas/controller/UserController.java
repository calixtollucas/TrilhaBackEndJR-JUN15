package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.model.user.UserAreasResponseDTO;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    AreaService areaService;

    @GetMapping("/areas")
    public ResponseEntity<UserAreasResponseDTO> getAreasFromUser(@RequestHeader(name = "Authorization") String token){
        String rawToken = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(tokenService.validateToken(rawToken));

        Set<Area> areas = areaService.getAllAreasFromUser(userId);
        if(areas == null){
            throw new BusinessException(AuthenticationException.class.getName(), "the user does not have areas");
        }

        //lista de AreaResponse
        Set<AreaResponseDTO> areasResponse = areas.stream()
                .map(area -> new AreaResponseDTO(area.getId(), area.getTitle()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new UserAreasResponseDTO(areasResponse));
    }
}

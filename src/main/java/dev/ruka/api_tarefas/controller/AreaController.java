package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaRequestPayload;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.JWTTokenService;
import dev.ruka.api_tarefas.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/areas")
public class AreaController {

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    AreaService areaService;

    @PostMapping
    public ResponseEntity<AreaResponseDTO> registerArea(@RequestBody @Valid AreaRequestPayload payload, @RequestHeader("Authorization") String token){
        token = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(tokenService.validateToken(token));
        User user = userService.findById(userId);

        Area newArea = new Area(payload.title(), user);

        Area saved = areaService.save(newArea);

        return ResponseEntity.ok(new AreaResponseDTO(saved.getId(), saved.getTitle(), saved.getUser().getUsername()));
    }

    /**
     *
     * @param token
     * @return "all the areas from the logged user"
     *
     * This endpoint returns a Set of all areas from a logged user
     */
    @GetMapping
    public ResponseEntity<Set<AreaResponseDTO>> getAllAreasFromUser(@RequestHeader("Authorization")String token){
        token = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(tokenService.validateToken(token));

        User user = userService.findById(userId);
        Set<Area> areasFound = areaService.findAllByUser(user);

        Set<AreaResponseDTO> areasDTO = areasFound.stream()
                .map((area) -> new AreaResponseDTO(area.getId(), area.getTitle(), area.getUser().getUsername()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(areasDTO);
    }
}

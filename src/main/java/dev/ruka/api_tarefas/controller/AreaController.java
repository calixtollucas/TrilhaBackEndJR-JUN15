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

import java.util.UUID;

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
    public ResponseEntity<AreaResponseDTO> registerArea(@RequestBody @Valid AreaRequestPayload payload, @RequestHeader(name = "Authorization") String token){
        Area newArea = new Area(payload.title());

        //persiste área no banco
        Area savedArea = areaService.save(newArea);

        //persiste área no banco
        token = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(tokenService.validateToken(token));
        User user = userService.findById(userId);

        areaService.addUserToArea(user, savedArea);

        return ResponseEntity.ok(new AreaResponseDTO(savedArea.getId(), savedArea.getTitle()));
    }

    @PutMapping("/{areaId}")
    public ResponseEntity<AreaResponseDTO> updateArea(@RequestBody @Valid AreaRequestPayload payload,@PathVariable UUID areaId){
        Area updatedArea = areaService.updateArea(areaId, payload);
        return ResponseEntity.ok(new AreaResponseDTO(updatedArea.getId(), updatedArea.getTitle()));
    }

    @DeleteMapping("/{areaId}")
    public ResponseEntity deleteArea(@PathVariable UUID areaId, @RequestHeader("Authorization") String token){

        token = token.replace("Bearer ", "");
        User user = userService.findById(
                UUID.fromString(tokenService.validateToken(token))
        );
        areaService.deleteAreaFromUser(areaId, user);
        return ResponseEntity.ok().build();
    }
}

package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.task.Task;
import dev.ruka.api_tarefas.model.task.TaskRequestPayload;
import dev.ruka.api_tarefas.model.task.TaskResponseDTO;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.JWTTokenService;
import dev.ruka.api_tarefas.services.TaskService;
import dev.ruka.api_tarefas.services.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    AreaService areaService;

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> registerTask(@RequestBody @Valid TaskRequestPayload payload, @RequestHeader("Authorization") String token){

        //captura área
        Area area = areaService.findAreaById(payload.areaId());
        //captura usuário
        token = token.replace("Bearer ", "");
        User user = userService.findById(UUID.fromString(tokenService.validateToken(token)));

        Task task = new Task(payload.taskTitle(), area, user, payload.important(), payload.urgent());

        taskService.save(task);

        return ResponseEntity.ok(new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getArea().getTitle(),
                task.getUser().getUsername(),
                task.getImportant(),
                task.getUrgent(),
                task.getComplete()
                )
        );
    }
}

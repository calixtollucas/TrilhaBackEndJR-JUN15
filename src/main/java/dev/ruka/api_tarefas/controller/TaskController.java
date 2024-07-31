package dev.ruka.api_tarefas.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    AreaService areaService;

    @Autowired
    UserService userService;

    @Autowired
    JWTTokenService tokenService;

    //REGISTER TASK
    @PostMapping
    public ResponseEntity<TaskResponseDTO> registerTask(@RequestBody @Valid TaskRequestPayload payload, @RequestHeader("Authorization") String token){

        token = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(tokenService.validateToken(token));
        User user = userService.findById(userId);

        Area area = areaService.findAreaById(payload.areaId());

        Task newTask = new Task(payload.taskTitle(), area, user, payload.important(), payload.urgent());
        taskService.save(newTask);

        return ResponseEntity.ok(new TaskResponseDTO(
                newTask.getId(),
                newTask.getTitle(),
                newTask.getArea().getTitle(),
                newTask.getUser().getUsername(),
                newTask.getImportant(),
                newTask.getUrgent(),
                newTask.getComplete()));
    }

    //GET TASKS FROM CURRENT USER
    @GetMapping
    public ResponseEntity<Set<TaskResponseDTO>> getAllTasksFromUser(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ", "");
        UUID userId = UUID.fromString(tokenService.validateToken(token));
        User user = userService.findById(userId);

        Set<Task> tasks = taskService.findAllByUser(user);

        Set<TaskResponseDTO> responsedto = tasks.stream()
                .map(
                        (task) -> new TaskResponseDTO(
                                task.getId(),
                                task.getTitle(),
                                task.getArea().getTitle(),
                                task.getUser().getUsername(),
                                task.getImportant(),
                                task.getUrgent(),
                                task.getComplete()
                        )
                )
                .collect(Collectors.toSet());

        return ResponseEntity.ok(responsedto);
    }

    //GET TASKS FROM AREA ID
    @GetMapping("/{areaId}")
    public ResponseEntity<Set<TaskResponseDTO>> getAllTasksFromArea(@PathVariable("areaId") UUID areaId){
        Area area = areaService.findAreaById(areaId);

        Set<Task> tasks = taskService.findAllByArea(area);

        Set<TaskResponseDTO> responseDto = tasks.stream()
                .map(
                        (task) -> new TaskResponseDTO(
                                task.getId(),
                                task.getTitle(),
                                task.getArea().getTitle(),
                                task.getUser().getUsername(),
                                task.getImportant(),
                                task.getUrgent(),
                                task.getComplete()
                        )
                )
                .collect(Collectors.toSet());

        return ResponseEntity.ok(responseDto);
    }

    //GET TASK FROM ID
    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskFromId(@PathVariable("taskId") UUID taskId){
        Task returned = taskService.findById(taskId);
        return ResponseEntity.ok(new TaskResponseDTO(
                returned.getId(),
                returned.getTitle(),
                returned.getArea().getTitle(),
                returned.getUser().getUsername(),
                returned.getImportant(),
                returned.getUrgent(),
                returned.getComplete()
        ));
    }

    //COMPLETE TASK
    @PutMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponseDTO> completeTask(@PathVariable("taskId") UUID taskId){
        Task task = taskService.complete(taskId);
        return ResponseEntity.ok(
                new TaskResponseDTO(task.getId(),
                        task.getTitle(),
                        task.getArea().getTitle(),
                        task.getUser().getUsername(),
                        task.getImportant(),
                        task.getUrgent(),
                        task.getComplete()
                        )
        );
    }

    //UPDATE TASK
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@RequestBody @Valid TaskRequestPayload payload, @PathVariable("taskId") UUID taskId){
        Task updated = taskService.update(payload, taskId);
        return ResponseEntity.ok(
                new TaskResponseDTO(
                        updated.getId(),
                        updated.getTitle(),
                        updated.getArea().getTitle(),
                        updated.getUser().getUsername(),
                        updated.getImportant(),
                        updated.getUrgent(),
                        updated.getComplete()
                )
        );
    }
}

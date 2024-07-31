package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.task.Task;
import dev.ruka.api_tarefas.model.task.TaskRequestPayload;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    AreaService areaService;

    @Autowired
    TaskRepository repository;

    public Task save(Task task){
        return repository.save(task);
    }

    public Set<Task> findAllByUser(User user) {
        Optional<Set<Task>> tasksReturned = repository.findAllByUser(user);
        return tasksReturned.orElseThrow(
                () -> new BusinessException(ChangeSetPersister.NotFoundException.class.getName(), "the user does not have tasks")
        );
    }

    public Set<Task> findAllByArea(Area area) {
        Optional<Set<Task>> returnedTasks = repository.findAllByArea(area);

        return returnedTasks.orElseThrow(
                () -> new BusinessException(ChangeSetPersister.NotFoundException.class.getName(), "The user does not have areas")
        );
    }

    public Task findById(UUID taskId) {
        Optional<Task> returnedTask = repository.findById(taskId);
        return returnedTask.orElseThrow(
                () -> new BusinessException(ChangeSetPersister.NotFoundException.class.getName(), "the task does not exists")
        );
    }

    public Task complete(UUID taskId) {
        Optional<Task> found = repository.findById(taskId);
        if(found.isPresent()){
            Task oldTask = found.get();
            oldTask.setComplete(true);
            return repository.save(oldTask);
        } else {
            throw new BusinessException(ChangeSetPersister.NotFoundException.class.getName(), "the task does not exists");
        }
    }

    public Task update(TaskRequestPayload payload, UUID taskId){
        Task found = this.findById(taskId);
        if(payload.areaId() != null){
            Area area = areaService.findAreaById(payload.areaId());
            found.setArea(area);
        }
        if(payload.taskTitle() != null) found.setTitle(payload.taskTitle());
        if(payload.important() != null) found.setImportant(payload.important() != 0);
        if(payload.urgent() != null) found.setUrgent(payload.urgent() != 0);

        repository.save(found);

        return found;
    }
}

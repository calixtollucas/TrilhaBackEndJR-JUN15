package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.model.task.Task;
import dev.ruka.api_tarefas.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository repository;

    public Task save(Task task){
        Task save = repository.save(task);
        return save;
    }
}

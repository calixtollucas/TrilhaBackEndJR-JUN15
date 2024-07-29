package dev.ruka.api_tarefas.repository;

import dev.ruka.api_tarefas.model.task.Task;
import dev.ruka.api_tarefas.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Set<Task>> findByUser(User user);
}

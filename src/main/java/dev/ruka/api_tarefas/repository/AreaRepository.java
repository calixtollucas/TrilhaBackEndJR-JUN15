package dev.ruka.api_tarefas.repository;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {

    Optional<Area> findByTitle(String title);

    Optional<Set<Area>> findByUser(User user);

    Optional<Set<Area>> findAllByUser(User user);
}

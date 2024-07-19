package dev.ruka.api_tarefas.repository;

import dev.ruka.api_tarefas.model.area.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {

    Optional<Area> findByTitle(String title);
}

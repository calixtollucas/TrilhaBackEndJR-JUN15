package dev.ruka.api_tarefas.repository;

import dev.ruka.api_tarefas.model.area.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {

    Optional<Area> findByTitle(String title);

    @Query("SELECT a FROM Area a JOIN users_areas ua WHERE ua.user_id = ?1")
    Optional<Set<Area>> findByUserId(UUID user);
}

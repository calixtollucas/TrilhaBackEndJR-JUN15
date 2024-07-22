package dev.ruka.api_tarefas.model.user;

import dev.ruka.api_tarefas.model.area.Area;

import java.util.Set;

public record UserResponseDTO(String name, Set<Area> areas) {
}

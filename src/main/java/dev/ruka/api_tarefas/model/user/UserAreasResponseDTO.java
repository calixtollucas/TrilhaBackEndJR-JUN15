package dev.ruka.api_tarefas.model.user;

import dev.ruka.api_tarefas.model.area.AreaResponseDTO;

import java.util.Set;

public record UserAreasResponseDTO(
        Set<AreaResponseDTO> user_areas
) {
}

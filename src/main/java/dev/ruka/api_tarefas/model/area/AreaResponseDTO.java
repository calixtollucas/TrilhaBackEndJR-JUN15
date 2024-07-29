package dev.ruka.api_tarefas.model.area;

import java.util.UUID;

public record AreaResponseDTO(
        UUID id,
        String title,
        String user) {
}

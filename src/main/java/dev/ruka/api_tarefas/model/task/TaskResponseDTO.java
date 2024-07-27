package dev.ruka.api_tarefas.model.task;

import java.util.UUID;

public record TaskResponseDTO(
        UUID task_id,
        String task_title,
        String task_area,
        String task_user,
        Boolean is_important,
        Boolean is_urgent,
        Boolean is_completed
) {
}

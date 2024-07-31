package dev.ruka.api_tarefas.model.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record TaskRequestPayload(
        String taskTitle,
        UUID areaId,
        Integer important,
        Integer urgent
) {
}

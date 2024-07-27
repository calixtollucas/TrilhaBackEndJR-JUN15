package dev.ruka.api_tarefas.model.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record TaskRequestPayload(
        @NotNull String taskTitle,
        @NotNull UUID areaId,
        @PositiveOrZero Integer important,
        @PositiveOrZero Integer urgent
) {
}

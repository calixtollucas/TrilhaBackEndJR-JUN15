package dev.ruka.api_tarefas.model.user;

import jakarta.validation.constraints.NotBlank;

public record UserRequestPayload(
        @NotBlank(message = "username cannot be empty or null")
        String username,
        @NotBlank(message = "username cannot be empty or null")
        String password
) {
}

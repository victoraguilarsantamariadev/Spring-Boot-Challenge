package com.usuario.usuarios_api.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO para actualizar un usuario existente")
public record UserUpdateDto(
        @Schema(description = "Nombre del usuario", example = "Juan", required = true)
        String firstName,
        @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
        String lastName,
        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com", required = true)
        String email,
        @Schema(description = "Rol del usuario", example = "ADMIN")
        String role,
        @Schema(description = "Estado del usuario", example = "ACTIVE")
        String status,
        @Schema(description = "Fecha de nacimiento del usuario", example = "1990-01-15")
        LocalDate birthDate
) {
}

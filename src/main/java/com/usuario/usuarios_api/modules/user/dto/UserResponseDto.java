package com.usuario.usuarios_api.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO de respuesta con la información del usuario")
public record UserResponseDto(
        @Schema(description = "ID del usuario", example = "1")
        Long id,
        @Schema(description = "Nombre del usuario", example = "Juan")
        String firstName,
        @Schema(description = "Apellido del usuario", example = "Pérez")
        String lastName,
        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
        String email,
        @Schema(description = "Rol del usuario", example = "ADMIN")
        String role,
        @Schema(description = "Estado del usuario", example = "ACTIVE")
        String status,
        @Schema(description = "Fecha de nacimiento del usuario", example = "1990-01-15")
        LocalDate birthDate,
        @Schema(description = "Fecha de creación del usuario", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,
        @Schema(description = "Fecha de última actualización del usuario", example = "2024-01-15T10:30:00")
        LocalDateTime updatedAt
) {
}

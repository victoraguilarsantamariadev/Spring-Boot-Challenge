package com.usuario.usuarios_api.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "Criterios de búsqueda para usuarios")
public record UserSearchRequest(
        @Schema(description = "Buscar por nombre o apellido", example = "Juan")
        String name,
        @Schema(description = "Buscar por email", example = "juan@example.com")
        String email,
        @Schema(description = "Filtrar por rol", example = "ADMIN")
        String role,
        @Schema(description = "Filtrar por estado", example = "ACTIVE")
        String status,
        @Schema(description = "Fecha de nacimiento desde (formato: yyyy-MM-dd)", example = "1990-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDateFrom,
        @Schema(description = "Fecha de nacimiento hasta (formato: yyyy-MM-dd)", example = "2000-12-31")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDateTo
) {
}



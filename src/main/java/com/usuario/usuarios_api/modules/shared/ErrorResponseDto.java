package com.usuario.usuarios_api.modules.shared;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para errores")
public record ErrorResponseDto(
        @Schema(description = "Mensaje de error", example = "Usuario no encontrado con id: 1")
        String message
) {
}
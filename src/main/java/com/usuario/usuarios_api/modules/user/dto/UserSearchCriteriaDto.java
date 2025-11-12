package com.usuario.usuarios_api.modules.user.dto;

import java.time.LocalDate;

public record UserSearchCriteriaDto(
        String name,
        String email,
        String role,
        String status,
        LocalDate birthDateFrom,
        LocalDate birthDateTo
) {
}

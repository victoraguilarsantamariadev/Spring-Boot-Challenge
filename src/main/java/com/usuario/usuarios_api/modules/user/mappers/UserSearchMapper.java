package com.usuario.usuarios_api.modules.user.mappers;

import com.usuario.usuarios_api.modules.user.dto.UserSearchCriteriaDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchRequest;

public final class UserSearchMapper {

    private UserSearchMapper() {
    }

    public static UserSearchCriteriaDto toCriteria(UserSearchRequest request) {
        return new UserSearchCriteriaDto(
                request.name(),
                request.email(),
                request.role(),
                request.status(),
                request.birthDateFrom(),
                request.birthDateTo()
        );
    }
}



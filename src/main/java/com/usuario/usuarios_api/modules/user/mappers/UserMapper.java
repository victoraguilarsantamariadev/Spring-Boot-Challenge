package com.usuario.usuarios_api.modules.user.mappers;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import com.usuario.usuarios_api.modules.user.entities.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserCreateDto dto) {
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setStatus(dto.status());
        user.setBirthDate(dto.birthDate());
        return user;
    }

    public static void updateEntity(User user, UserUpdateDto dto) {
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setStatus(dto.status());
        user.setBirthDate(dto.birthDate());
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getBirthDate(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
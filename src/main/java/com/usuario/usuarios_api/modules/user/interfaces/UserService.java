package com.usuario.usuarios_api.modules.user.interfaces;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchCriteriaDto;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDto create(UserCreateDto dto);

    UserResponseDto getById(Long id);

    UserResponseDto update(Long id, UserUpdateDto dto);

    void delete(Long id);

    Page<UserResponseDto> search(UserSearchCriteriaDto criteria, Pageable pageable);
}
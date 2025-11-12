package com.usuario.usuarios_api.modules.user.controllers;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchRequest;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import com.usuario.usuarios_api.modules.user.interfaces.UserService;
import com.usuario.usuarios_api.modules.user.mappers.UserSearchMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API para gestión de usuarios")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "El email ya existe")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @Operation(summary = "Obtener usuario por ID", description = "Obtiene los detalles de un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "El email ya existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuarios", description = "Busca usuarios con filtros, orden y paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> search(
            @Parameter(description = "Criterios de búsqueda (nombre, email, role, status, fechas)")
            UserSearchRequest request,
            @Parameter(description = "Parámetros de paginación (page, size, sort)")
            Pageable pageable) {
        return ResponseEntity.ok(userService.search(UserSearchMapper.toCriteria(request), pageable));
    }
}


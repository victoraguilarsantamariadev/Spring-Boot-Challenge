package com.usuario.usuarios_api.modules.user.mappers;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import com.usuario.usuarios_api.modules.user.entities.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class UserMapperTest {

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private User user;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Before
    public void setUp() {
        birthDate = LocalDate.of(2000, 1, 1);
        createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        updatedAt = LocalDateTime.of(2024, 1, 2, 10, 0, 0);

        userCreateDto = new UserCreateDto(
                "Hector",
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                birthDate
        );

        userUpdateDto = new UserUpdateDto(
                "Hector Updated",
                "Castro Updated",
                "hector.updated@example.com",
                "USER",
                "INACTIVE",
                LocalDate.of(1995, 5, 15)
        );

        user = new User();
        user.setId(1L);
        user.setFirstName("Hector");
        user.setLastName("Castro");
        user.setEmail("hector@example.com");
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");
        user.setBirthDate(birthDate);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
    }

    @Test
    public void testToEntityOk() {
        User result = UserMapper.toEntity(userCreateDto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(userCreateDto.firstName(), result.getFirstName());
        assertEquals(userCreateDto.lastName(), result.getLastName());
        assertEquals(userCreateDto.email(), result.getEmail());
        assertEquals(userCreateDto.role(), result.getRole());
        assertEquals(userCreateDto.status(), result.getStatus());
        assertEquals(userCreateDto.birthDate(), result.getBirthDate());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    public void testToEntityWithNullFields() {
        UserCreateDto dtoWithNulls = new UserCreateDto(
                null,
                null,
                null,
                null,
                null,
                null
        );

        User result = UserMapper.toEntity(dtoWithNulls);

        assertNotNull(result);
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
        assertNull(result.getRole());
        assertNull(result.getStatus());
        assertNull(result.getBirthDate());
    }

    @Test
    public void testUpdateEntityOk() {
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setFirstName("Original");
        originalUser.setLastName("Name");
        originalUser.setEmail("original@example.com");
        originalUser.setRole("USER");
        originalUser.setStatus("ACTIVE");
        originalUser.setBirthDate(LocalDate.of(1990, 1, 1));
        originalUser.setCreatedAt(createdAt);
        originalUser.setUpdatedAt(updatedAt);

        UserMapper.updateEntity(originalUser, userUpdateDto);

        assertEquals(1L, originalUser.getId().longValue());
        assertEquals(userUpdateDto.firstName(), originalUser.getFirstName());
        assertEquals(userUpdateDto.lastName(), originalUser.getLastName());
        assertEquals(userUpdateDto.email(), originalUser.getEmail());
        assertEquals(userUpdateDto.role(), originalUser.getRole());
        assertEquals(userUpdateDto.status(), originalUser.getStatus());
        assertEquals(userUpdateDto.birthDate(), originalUser.getBirthDate());
        assertEquals(createdAt, originalUser.getCreatedAt());
        assertEquals(updatedAt, originalUser.getUpdatedAt());
    }

    @Test
    public void testUpdateEntityWithNullFields() {
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setFirstName("Original");
        originalUser.setEmail("original@example.com");
        originalUser.setCreatedAt(createdAt);

        UserUpdateDto dtoWithNulls = new UserUpdateDto(
                null,
                null,
                null,
                null,
                null,
                null
        );

        UserMapper.updateEntity(originalUser, dtoWithNulls);

        assertEquals(1L, originalUser.getId().longValue());
        assertNull(originalUser.getFirstName());
        assertNull(originalUser.getLastName());
        assertNull(originalUser.getEmail());
        assertNull(originalUser.getRole());
        assertNull(originalUser.getStatus());
        assertNull(originalUser.getBirthDate());
        assertEquals(createdAt, originalUser.getCreatedAt());
    }

    @Test
    public void testToDtoOk() {
        UserResponseDto result = UserMapper.toDto(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getRole(), result.role());
        assertEquals(user.getStatus(), result.status());
        assertEquals(user.getBirthDate(), result.birthDate());
        assertEquals(user.getCreatedAt(), result.createdAt());
        assertEquals(user.getUpdatedAt(), result.updatedAt());
    }

    @Test
    public void testToDtoWithNullFields() {
        User userWithNulls = new User();
        userWithNulls.setId(1L);

        UserResponseDto result = UserMapper.toDto(userWithNulls);

        assertNotNull(result);
        assertEquals(1L, result.id().longValue());
        assertNull(result.firstName());
        assertNull(result.lastName());
        assertNull(result.email());
        assertNull(result.role());
        assertNull(result.status());
        assertNull(result.birthDate());
        assertNull(result.createdAt());
        assertNull(result.updatedAt());
    }

    @Test
    public void testCompleteMappingCycle() {
        User entity = UserMapper.toEntity(userCreateDto);
        entity.setId(1L);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        UserResponseDto dto = UserMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(userCreateDto.firstName(), dto.firstName());
        assertEquals(userCreateDto.lastName(), dto.lastName());
        assertEquals(userCreateDto.email(), dto.email());
        assertEquals(userCreateDto.role(), dto.role());
        assertEquals(userCreateDto.status(), dto.status());
        assertEquals(userCreateDto.birthDate(), dto.birthDate());
    }
}


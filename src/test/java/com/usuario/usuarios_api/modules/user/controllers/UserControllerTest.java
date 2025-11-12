package com.usuario.usuarios_api.modules.user.controllers;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchRequest;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import com.usuario.usuarios_api.modules.user.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserResponseDto userResponseDto;
    private Long userId;

    @Before
    public void setUp() {
        userId = 1L;
        userCreateDto = new UserCreateDto(
                "Hector",
                "Castro",
                "s0vT6@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userUpdateDto = new UserUpdateDto(
                "Hector",
                "Castro Updated",
                "hector@example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userResponseDto = new UserResponseDto(
                userId,
                "Hector",
                "Castro",
                "s0vT6@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testCreateUserOk() {
        when(userService.create(any(UserCreateDto.class))).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.create(userCreateDto);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).create(userCreateDto);
    }

    @Test
    public void testGetByIdOk() {
        when(userService.getById(userId)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.getById(userId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(userResponseDto, response.getBody());
        assertEquals(userId, response.getBody().id());
        verify(userService, times(1)).getById(userId);
    }

    @Test
    public void testUpdateOk() {
        UserResponseDto updatedResponse = new UserResponseDto(
                userId,
                userUpdateDto.firstName(),
                userUpdateDto.lastName(),
                userUpdateDto.email(),
                userUpdateDto.role(),
                userUpdateDto.status(),
                userUpdateDto.birthDate(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(userService.update(eq(userId), any(UserUpdateDto.class))).thenReturn(updatedResponse);

        ResponseEntity<UserResponseDto> response = userController.update(userId, userUpdateDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(updatedResponse, response.getBody());
        verify(userService, times(1)).update(userId, userUpdateDto);
    }

    @Test
    public void testDeleteOk() {
        doNothing().when(userService).delete(userId);

        ResponseEntity<Void> response = userController.delete(userId);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
        assertNull(response.getBody());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testSearchOk() {
        UserSearchRequest searchRequest = new UserSearchRequest(
                "Hector",
                null,
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponseDto> page = new PageImpl<>(List.of(userResponseDto), pageable, 1);
        
        when(userService.search(any(), eq(pageable))).thenReturn(page);

        ResponseEntity<Page<UserResponseDto>> response = userController.search(searchRequest, pageable);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(userResponseDto, response.getBody().getContent().get(0));
        verify(userService, times(1)).search(any(), eq(pageable));
    }

    @Test
    public void testSearchEmptyResults() {
        UserSearchRequest searchRequest = new UserSearchRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponseDto> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        when(userService.search(any(), eq(pageable))).thenReturn(emptyPage);

        ResponseEntity<Page<UserResponseDto>> response = userController.search(searchRequest, pageable);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
        assertTrue(response.getBody().getContent().isEmpty());
        verify(userService, times(1)).search(any(), eq(pageable));
    }
}

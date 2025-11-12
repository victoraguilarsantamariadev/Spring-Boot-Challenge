package com.usuario.usuarios_api.modules.user.services;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchCriteriaDto;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import com.usuario.usuarios_api.modules.user.entities.User;
import com.usuario.usuarios_api.modules.user.exceptions.InvalidUserDataException;
import com.usuario.usuarios_api.modules.user.exceptions.UserAlreadyExistsException;
import com.usuario.usuarios_api.modules.user.exceptions.UserNotFoundException;
import com.usuario.usuarios_api.modules.user.repositories.UserRepository;
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
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserResponseDto userResponseDto;
    private Long userId;

    @Before
    public void setUp() {
        userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        user = new User();
        user.setId(userId);
        user.setFirstName("Hector");
        user.setLastName("Castro");
        user.setEmail("hector@example.com");
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userCreateDto = new UserCreateDto(
                "Hector",
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userUpdateDto = new UserUpdateDto(
                "Hector",
                "Castro Updated",
                "hector.updated@example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userResponseDto = new UserResponseDto(
                userId,
                "Hector",
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1),
                now,
                now
        );
    }

    @Test
    public void testCreateUserOk() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto result = userService.create(userCreateDto);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals(userCreateDto.firstName(), result.firstName());
        assertEquals(userCreateDto.email(), result.email());
        verify(userRepository, times(1)).findByEmailIgnoreCase(userCreateDto.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithMissingFirstName() {
        UserCreateDto invalidDto = new UserCreateDto(
                "",
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithMissingLastName() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithMissingEmail() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithWhitespaceOnlyFirstName() {
        UserCreateDto invalidDto = new UserCreateDto(
                "   ",
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithWhitespaceOnlyLastName() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "   ",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithWhitespaceOnlyEmail() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "   ",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithTabOnlyEmail() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "\t",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithNewlineOnlyEmail() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "\n",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test
    public void testCreateUserWithEmailContainingOnlySpacesAndAt() {
        User userWithSpacesAndAt = new User();
        userWithSpacesAndAt.setId(9L);
        userWithSpacesAndAt.setFirstName("Test");
        userWithSpacesAndAt.setLastName("User");
        userWithSpacesAndAt.setEmail(" @ ");

        UserCreateDto dtoWithSpacesAndAt = new UserCreateDto(
                "Test",
                "User",
                " @ ",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase(" @ ")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithSpacesAndAt);

        UserResponseDto result = userService.create(dtoWithSpacesAndAt);

        assertNotNull(result);
        assertEquals(" @ ", result.email());
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithInvalidEmailNoAtSymbol() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "invalid-email",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithEmailOnlyTextNoAt() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "justtext",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithEmailWithNumbersNoAt() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "test123example",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithEmailWithSpecialCharsNoAt() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                "test.example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test
    public void testCreateUserWithEmailOnlyAtSymbol() {
        User userWithAt = new User();
        userWithAt.setId(2L);
        userWithAt.setFirstName("Test");
        userWithAt.setLastName("User");
        userWithAt.setEmail("@");
        userWithAt.setRole("USER");
        userWithAt.setStatus("ACTIVE");

        UserCreateDto dtoWithAt = new UserCreateDto(
                "Test",
                "User",
                "@",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase("@")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithAt);

        UserResponseDto result = userService.create(dtoWithAt);

        assertNotNull(result);
        assertEquals("@", result.email());
    }

    @Test
    public void testCreateUserWithEmailStartingWithAt() {
        User userWithAtStart = new User();
        userWithAtStart.setId(3L);
        userWithAtStart.setFirstName("Test");
        userWithAtStart.setLastName("User");
        userWithAtStart.setEmail("@example.com");

        UserCreateDto dtoWithAtStart = new UserCreateDto(
                "Test",
                "User",
                "@example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase("@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithAtStart);

        UserResponseDto result = userService.create(dtoWithAtStart);

        assertNotNull(result);
        assertEquals("@example.com", result.email());
    }

    @Test
    public void testCreateUserWithEmailEndingWithAt() {
        User userWithAtEnd = new User();
        userWithAtEnd.setId(4L);
        userWithAtEnd.setFirstName("Test");
        userWithAtEnd.setLastName("User");
        userWithAtEnd.setEmail("hector@");

        UserCreateDto dtoWithAtEnd = new UserCreateDto(
                "Test",
                "User",
                "hector@",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase("hector@")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithAtEnd);

        UserResponseDto result = userService.create(dtoWithAtEnd);

        assertNotNull(result);
        assertEquals("hector@", result.email());
    }

    @Test
    public void testCreateUserWithValidEmailFormat() {
        when(userRepository.findByEmailIgnoreCase("valid@example.com")).thenReturn(Optional.empty());
        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setFirstName("Hector");
        savedUser.setLastName("Castro");
        savedUser.setEmail("valid@example.com");
        savedUser.setRole("ADMIN");
        savedUser.setStatus("ACTIVE");
        savedUser.setBirthDate(LocalDate.of(2000, 1, 1));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserCreateDto validDto = new UserCreateDto(
                "Hector",
                "Castro",
                "valid@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        UserResponseDto result = userService.create(validDto);

        assertNotNull(result);
        assertEquals("valid@example.com", result.email());
        verify(userRepository, times(1)).findByEmailIgnoreCase("valid@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUserWithEmailContainingSpaces() {
        User userWithSpaces = new User();
        userWithSpaces.setId(5L);
        userWithSpaces.setFirstName("Test");
        userWithSpaces.setLastName("User");
        userWithSpaces.setEmail("test @example.com");

        UserCreateDto dtoWithSpaces = new UserCreateDto(
                "Test",
                "User",
                "test @example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase("test @example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithSpaces);

        UserResponseDto result = userService.create(dtoWithSpaces);

        assertNotNull(result);
        assertEquals("test @example.com", result.email());
    }

    @Test
    public void testCreateUserWithEmailWithSpacesAroundAt() {
        User userWithSpacesAround = new User();
        userWithSpacesAround.setId(6L);
        userWithSpacesAround.setFirstName("Test");
        userWithSpacesAround.setLastName("User");
        userWithSpacesAround.setEmail("test @ example.com");

        UserCreateDto dtoWithSpacesAround = new UserCreateDto(
                "Test",
                "User",
                "test @ example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase("test @ example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithSpacesAround);

        UserResponseDto result = userService.create(dtoWithSpacesAround);

        assertNotNull(result);
        assertEquals("test @ example.com", result.email());
    }

    @Test
    public void testCreateUserWithEmailWithLeadingTrailingSpaces() {
        User userWithLeadingTrailing = new User();
        userWithLeadingTrailing.setId(7L);
        userWithLeadingTrailing.setFirstName("Test");
        userWithLeadingTrailing.setLastName("User");
        userWithLeadingTrailing.setEmail(" test@example.com ");

        UserCreateDto dtoWithLeadingTrailing = new UserCreateDto(
                "Test",
                "User",
                " test@example.com ",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase(" test@example.com ")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithLeadingTrailing);

        UserResponseDto result = userService.create(dtoWithLeadingTrailing);

        assertNotNull(result);
        assertEquals(" test@example.com ", result.email());
    }

    @Test
    public void testCreateUserWithEmailMultipleAtSymbols() {
        User userMultipleAt = new User();
        userMultipleAt.setId(8L);
        userMultipleAt.setFirstName("Test");
        userMultipleAt.setLastName("User");
        userMultipleAt.setEmail("test@@example.com");

        UserCreateDto dtoMultipleAt = new UserCreateDto(
                "Test",
                "User",
                "test@@example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        when(userRepository.findByEmailIgnoreCase("test@@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userMultipleAt);

        UserResponseDto result = userService.create(dtoMultipleAt);

        assertNotNull(result);
        assertEquals("test@@example.com", result.email());
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithNullFirstName() {
        UserCreateDto invalidDto = new UserCreateDto(
                null,
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithNullLastName() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                null,
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testCreateUserWithNullEmail() {
        UserCreateDto invalidDto = new UserCreateDto(
                "Hector",
                "Castro",
                null,
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.create(invalidDto);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateUserWithDuplicateEmail() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        userService.create(userCreateDto);
    }

    @Test
    public void testGetByIdOk() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals(user.getEmail(), result.email());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetByIdNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.getById(userId);
    }

    @Test
    public void testUpdateUserOk() {
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName(userUpdateDto.firstName());
        updatedUser.setLastName(userUpdateDto.lastName());
        updatedUser.setEmail(userUpdateDto.email());
        updatedUser.setRole(userUpdateDto.role());
        updatedUser.setStatus(userUpdateDto.status());
        updatedUser.setBirthDate(userUpdateDto.birthDate());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase(userUpdateDto.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto result = userService.update(userId, userUpdateDto);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals(userUpdateDto.email(), result.email());
        assertEquals(userUpdateDto.lastName(), result.lastName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findByEmailIgnoreCase(userUpdateDto.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUserWithSameEmail() {
        UserUpdateDto sameEmailDto = new UserUpdateDto(
                "Hector",
                "Castro Updated",
                user.getEmail(),
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName(sameEmailDto.firstName());
        updatedUser.setLastName(sameEmailDto.lastName());
        updatedUser.setEmail(sameEmailDto.email());
        updatedUser.setRole(sameEmailDto.role());
        updatedUser.setStatus(sameEmailDto.status());
        updatedUser.setBirthDate(sameEmailDto.birthDate());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto result = userService.update(userId, sameEmailDto);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals(user.getEmail(), result.email());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.update(userId, userUpdateDto);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testUpdateUserWithDuplicateEmail() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail(userUpdateDto.email());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase(userUpdateDto.email())).thenReturn(Optional.of(otherUser));

        userService.update(userId, userUpdateDto);
    }

    @Test
    public void testDeleteUserOk() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        userService.delete(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeleteUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.delete(userId);
    }

    @Test
    public void testSearchWithFilters() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                "Hector",
                null,
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithoutFilters() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithEmptyResults() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                "NonExistent",
                null,
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(emptyPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithMissingFirstName() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "",
                "Castro",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithMissingLastName() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithInvalidEmailNoAtSymbol() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "invalid-email",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithNullEmail() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                null,
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithEmptyEmail() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithWhitespaceOnlyEmail() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "   ",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithTabOnlyEmail() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "\t",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithNewlineOnlyEmail() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "\n",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithEmailOnlyTextNoAt() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "justtext",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithEmailWithNumbersNoAt() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "test123example",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test(expected = InvalidUserDataException.class)
    public void testUpdateUserWithEmailWithSpecialCharsNoAt() {
        UserUpdateDto invalidDto = new UserUpdateDto(
                "Hector",
                "Castro",
                "test.example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        userService.update(userId, invalidDto);
    }

    @Test
    public void testUpdateUserWithEmailOnlyAtSymbol() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase("@")).thenReturn(Optional.empty());
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("@");
        updatedUser.setFirstName("Test");
        updatedUser.setLastName("User");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserUpdateDto dtoWithAt = new UserUpdateDto(
                "Test",
                "User",
                "@",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        UserResponseDto result = userService.update(userId, dtoWithAt);

        assertNotNull(result);
        assertEquals("@", result.email());
    }

    @Test
    public void testUpdateUserWithEmailStartingWithAt() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase("@example.com")).thenReturn(Optional.empty());
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("@example.com");
        updatedUser.setFirstName("Test");
        updatedUser.setLastName("User");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserUpdateDto dtoWithAtStart = new UserUpdateDto(
                "Test",
                "User",
                "@example.com",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        UserResponseDto result = userService.update(userId, dtoWithAtStart);

        assertNotNull(result);
        assertEquals("@example.com", result.email());
    }

    @Test
    public void testUpdateUserWithEmailEndingWithAt() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase("hector@")).thenReturn(Optional.empty());
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("hector@");
        updatedUser.setFirstName("Test");
        updatedUser.setLastName("User");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserUpdateDto dtoWithAtEnd = new UserUpdateDto(
                "Test",
                "User",
                "hector@",
                "USER",
                "ACTIVE",
                LocalDate.of(2000, 1, 1)
        );

        UserResponseDto result = userService.update(userId, dtoWithAtEnd);

        assertNotNull(result);
        assertEquals("hector@", result.email());
    }

    @Test
    public void testSearchWithEmailFilter() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                null,
                "hector@example.com",
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithRoleFilter() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                null,
                null,
                "ADMIN",
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithStatusFilter() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                null,
                null,
                null,
                "ACTIVE",
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithBirthDateFromFilter() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                null,
                null,
                null,
                null,
                LocalDate.of(1990, 1, 1),
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithBirthDateToFilter() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                null,
                null,
                null,
                null,
                null,
                LocalDate.of(2010, 12, 31)
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }

    @Test
    public void testSearchWithMultipleFilters() {
        UserSearchCriteriaDto criteria = new UserSearchCriteriaDto(
                "Hector",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2010, 12, 31)
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserResponseDto> result = userService.search(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(nullable(Specification.class), eq(pageable));
    }
}

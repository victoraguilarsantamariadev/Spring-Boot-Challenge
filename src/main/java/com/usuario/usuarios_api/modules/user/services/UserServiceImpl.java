package com.usuario.usuarios_api.modules.user.services;

import com.usuario.usuarios_api.modules.user.dto.UserCreateDto;
import com.usuario.usuarios_api.modules.user.dto.UserResponseDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchCriteriaDto;
import com.usuario.usuarios_api.modules.user.dto.UserUpdateDto;
import com.usuario.usuarios_api.modules.user.entities.User;
import com.usuario.usuarios_api.modules.user.exceptions.InvalidUserDataException;
import com.usuario.usuarios_api.modules.user.exceptions.UserAlreadyExistsException;
import com.usuario.usuarios_api.modules.user.exceptions.UserNotFoundException;
import com.usuario.usuarios_api.modules.user.interfaces.UserService;
import com.usuario.usuarios_api.modules.user.mappers.UserMapper;
import com.usuario.usuarios_api.modules.user.repositories.UserRepository;
import com.usuario.usuarios_api.modules.user.specifications.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto create(UserCreateDto dto) {
        validateMandatoryFields(dto.firstName(), dto.lastName(), dto.email());
        validateEmail(dto.email());
        ensureEmailUnique(dto.email(), null);

        User user = UserMapper.toEntity(dto);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getById(Long id) {
        return UserMapper.toDto(findUser(id));
    }

    @Override
    public UserResponseDto update(Long id, UserUpdateDto dto) {
        validateMandatoryFields(dto.firstName(), dto.lastName(), dto.email());
        validateEmail(dto.email());

        User user = findUser(id);
        ensureEmailUnique(dto.email(), id);

        UserMapper.updateEntity(user, dto);
        user.setUpdatedAt(LocalDateTime.now());

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(findUser(id));
    }

    @Override
    public Page<UserResponseDto> search(UserSearchCriteriaDto criteria, Pageable pageable) {
        List<Specification<User>> specifications = new ArrayList<>();

        addIfHasText(specifications, criteria.name(), UserSpecifications::nameContains);
        addIfHasText(specifications, criteria.email(), UserSpecifications::emailContains);
        addIfHasText(specifications, criteria.role(), UserSpecifications::roleEquals);
        addIfHasText(specifications, criteria.status(), UserSpecifications::statusEquals);
        addIfPresent(specifications, criteria.birthDateFrom(), UserSpecifications::birthDateFrom);
        addIfPresent(specifications, criteria.birthDateTo(), UserSpecifications::birthDateTo);

        Specification<User> combinedSpec = specifications.stream()
                .reduce(Specification::and)
                .orElse(null);

        return userRepository.findAll(combinedSpec, pageable)
                .map(UserMapper::toDto);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void ensureEmailUnique(String email, Long currentId) {
        userRepository.findByEmailIgnoreCase(email)
                .filter(existing -> !Objects.equals(existing.getId(), currentId))
                .ifPresent(existing -> {
                    throw new UserAlreadyExistsException(email);
                });
    }

    private void validateMandatoryFields(String firstName, String lastName, String email) {
        if (!StringUtils.hasText(firstName)
                || !StringUtils.hasText(lastName)
                || !StringUtils.hasText(email)) {
            throw new InvalidUserDataException("Nombre, apellido y email son obligatorios.");
        }
    }

    private void validateEmail(String email) {
        if (!email.contains("@")) {
            throw new InvalidUserDataException("Email inválido.");
        }
    }

    private void addIfHasText(List<Specification<User>> specifications,
                              String value,
                              java.util.function.Function<String, Specification<User>> supplier) {
        if (StringUtils.hasText(value)) {
            specifications.add(supplier.apply(value));
        }
    }

    private <T> void addIfPresent(List<Specification<User>> specifications,
                                  T value,
                                  java.util.function.Function<T, Specification<User>> supplier) {
        if (Objects.nonNull(value)) {
            specifications.add(supplier.apply(value));
        }
    }
}
package com.usuario.usuarios_api.modules.user.specifications;

import com.usuario.usuarios_api.modules.user.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> nameContains(String value) {
        String likeValue = wrapLike(value);
        return (root, query, builder) ->
                builder.or(
                        builder.like(builder.lower(root.get("firstName")), likeValue),
                        builder.like(builder.lower(root.get("lastName")), likeValue)
                );
    }

    public static Specification<User> emailContains(String value) {
        String likeValue = wrapLike(value);
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("email")), likeValue);
    }

    public static Specification<User> roleEquals(String role) {
        String normalized = role.trim().toLowerCase();
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("role")), normalized);
    }

    public static Specification<User> statusEquals(String status) {
        String normalized = status.trim().toLowerCase();
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("status")), normalized);
    }

    public static Specification<User> birthDateFrom(LocalDate from) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("birthDate"), from);
    }

    public static Specification<User> birthDateTo(LocalDate to) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("birthDate"), to);
    }

    private static String wrapLike(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}



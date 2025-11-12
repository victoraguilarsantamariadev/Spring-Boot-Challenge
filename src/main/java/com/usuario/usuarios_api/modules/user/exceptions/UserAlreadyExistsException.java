package com.usuario.usuarios_api.modules.user.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("Ya existe un usuario con el email: " + email);
    }
}



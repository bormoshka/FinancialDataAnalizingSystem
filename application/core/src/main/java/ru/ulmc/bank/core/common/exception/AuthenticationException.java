package ru.ulmc.bank.core.common.exception;

import lombok.Getter;

public class AuthenticationException extends Exception {
    @Getter private boolean isSystemFault = false;
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(Throwable cause) {
        super("Ошибка аутентификации!", cause);
        isSystemFault = true;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        isSystemFault = true;
    }

}

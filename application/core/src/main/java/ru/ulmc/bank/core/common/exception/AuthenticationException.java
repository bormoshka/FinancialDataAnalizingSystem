package ru.ulmc.bank.core.common.exception;

import lombok.Getter;

public class AuthenticationException extends Exception {
    @Getter private boolean isSystemFault = false;
    private static final String DEFAULT_MESSAGE = "Ошибка аутентификации!";

    public AuthenticationException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        isSystemFault = true;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        isSystemFault = true;
    }

}

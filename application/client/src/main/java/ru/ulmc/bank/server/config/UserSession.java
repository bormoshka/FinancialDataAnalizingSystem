package ru.ulmc.bank.server.config;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.ulmc.bank.dao.entity.system.User;

/**
 * Хранит общие параметры сессии пользователя.
 */
@Data
@Component
@Scope(scopeName = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {
    private User user = null;

    public boolean isAuthenticated() {
        return user != null;
    }
}

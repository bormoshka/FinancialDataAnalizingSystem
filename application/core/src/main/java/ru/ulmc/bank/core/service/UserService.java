package ru.ulmc.bank.core.service;

import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.dao.entity.system.User;

/**
 * Сервис доступа к даннм пользователей
 */
public interface UserService {
    User getUserByLoginAndEncodedPassword(String login, String encodedPassword) throws AuthenticationException;
}

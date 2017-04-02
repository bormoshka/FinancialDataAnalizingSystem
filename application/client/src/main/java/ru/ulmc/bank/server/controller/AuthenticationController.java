package ru.ulmc.bank.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.core.service.UserService;
import ru.ulmc.bank.core.service.impl.UserServiceImpl;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.server.config.UserSession;

/**
 * Контроллер аутентификации
 */
@Controller
public class AuthenticationController {

    private UserSession userSession;

    private UserService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(UserSession userSession,
                                    UserServiceImpl userDetailsService,
                                    PasswordEncoder passwordEncoder) {
        this.userSession = userSession;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String login, String password) throws AuthenticationException {
        User user =  userDetailsService.getUserByLoginAndEncodedPassword(login, encodePassword(password));
        if (user != null) {
            userSession.setUser(user);
        }
        return user;
    }

    private String encodePassword(String decodedPassword) {
        return passwordEncoder.encode(decodedPassword);
    }
}

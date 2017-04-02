package ru.ulmc.bank.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.core.service.UserService;
import ru.ulmc.bank.core.service.impl.UserServiceImpl;
import ru.ulmc.bank.dao.entity.system.User;

/**
 * Created by User on 02.04.2017.
 */
@Controller
public class AuthenticationController {

    UserService userDetailsService;

    @Autowired
    public AuthenticationController(UserServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public User authenticate(String login, String password) throws AuthenticationException {
       return userDetailsService.getUserByLoginAndEncodedPassword(login, encodePassword(password));
    }

    private String encodePassword(String decodedPassword) {
        return decodedPassword;
    }
}

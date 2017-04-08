package ru.ulmc.bank.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.core.common.security.UserPrincipal;
import ru.ulmc.bank.dao.entity.system.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер аутентификации
 */
@Controller
public class AuthenticationController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public User authenticate(String login, String password, HttpServletRequest httpRequest) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
            token.setDetails(new WebAuthenticationDetails(httpRequest));
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ((UserPrincipal) (authentication).getPrincipal()).getUser();
        } catch (Exception ex) {
            LOG.error("Auth error", ex);
            throw new AuthenticationException(ex);
        }
    }
}

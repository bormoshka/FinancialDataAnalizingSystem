package ru.ulmc.bank.server.rest;

//import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ulmc.bank.server.config.UserSession;


/**
 * IndexController
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {
    private static final String VIEW = "index";

    private UserSession userSession;

    @Autowired
    public IndexController(UserSession userSession) {
        this.userSession = userSession;
    }

    @RequestMapping(value = "*", method = RequestMethod.GET)
    public String index(Model data) {
        return userSession.isAuthenticated() ? "redirect:app/" : "redirect:auth/";
    }
}

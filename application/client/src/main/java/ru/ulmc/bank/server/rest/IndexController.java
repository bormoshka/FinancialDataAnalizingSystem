package ru.ulmc.bank.server.rest;

//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * IndexController
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {
    private static final String VIEW = "index";

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Transactional
    //@PreAuthorize(value = Roles.ADMIN)
    public String index(Model data) {

        return VIEW;
    }

    @GetMapping(value = "change")
    @Transactional
    public String change(Model data) {

        data.addAttribute("message", "OK ");
        return VIEW;
    }

}

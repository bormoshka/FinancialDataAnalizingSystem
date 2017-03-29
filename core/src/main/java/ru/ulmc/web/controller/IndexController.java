package ru.ulmc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ulmc.dao.entity.financial.Currency;
import ru.ulmc.dao.service.CurrencyService;

/**
 * IndexController
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {
    private static final String VIEW = "index";
    private final CurrencyService currencyService;

    @Autowired
    public IndexController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Transactional
    public String index(Model data) {

        currencyService.createCurrency("RUB", "Russian Rubles", 100);
        currencyService.createCurrency("AUD", "Австралийский доллар", 10);
        currencyService.createCurrency("GBP", "Фунты", 4);
        Currency cur = currencyService.createCurrency("BYR", "Картошка", 1);

        data.addAttribute("message", "OK ");
        return VIEW;
    }

    @RequestMapping(value = "change", method = RequestMethod.GET)
    @Transactional
    public String change(Model data) {
        Currency cur = currencyService.findCurrency("BYR");
        currencyService.changeCurrencyAttributes(cur, "Картошка-мартошка", 1);
        currencyService.changeCurrencyAttributes(cur.getId(), 3);
        currencyService.changeCurrencyAttributes(cur, 2);
        currencyService.changeCurrencyAttributes(cur, 5);

        data.addAttribute("message", "OK ");
        return VIEW;
    }

}

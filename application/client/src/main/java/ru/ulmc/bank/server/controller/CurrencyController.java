package ru.ulmc.bank.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.ulmc.bank.core.common.Perms;
import ru.ulmc.bank.dao.entity.financial.Currency;
import ru.ulmc.bank.dao.repository.CurrencyRepository;

/**
 * Created by User on 03.04.2017.
 */
@Controller
public class CurrencyController {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @PreAuthorize(Perms.CHECK_FIN_CURRENCY_READ)
    public Currency getCurrency(String code) {
        logger.debug("HERE WE ARE!");
        return currencyRepository.findByCode(code);
    }
}

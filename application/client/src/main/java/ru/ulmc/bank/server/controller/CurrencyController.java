package ru.ulmc.bank.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ru.ulmc.bank.core.common.Perms;
import ru.ulmc.bank.dao.entity.financial.Currency;
import ru.ulmc.bank.dao.repository.CurrencyRepository;
import ru.ulmc.bank.pojo.CurrencyPairDto;

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

    @PreAuthorize(Perms.CHECK_FIN_CURRENCY_READ)
    public List<CurrencyPairDto> getAllCurrencyPairs() {
        logger.debug("HERE WE ARE!");
        CurrencyPairDto currencyPairDto = new CurrencyPairDto();
        return Collections.singletonList(currencyPairDto);
    }

    @PreAuthorize(Perms.CHECK_FIN_CURRENCY_WRITE)
    public void removeCurrencyPairs(Collection<CurrencyPairDto> remove) {
    }
}

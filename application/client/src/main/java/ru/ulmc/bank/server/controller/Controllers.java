package ru.ulmc.bank.server.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by User on 03.04.2017.
 */
@Component
@Getter
public class Controllers {
    private final AuthenticationController authenticationController;
    private final CurrencyController currencyController;
    private final PublishingController publishingController;
    private final LdapController ldapController;

    @Autowired
    public Controllers(AuthenticationController authenticationController,
                       CurrencyController currencyController,
                       PublishingController publishingController,
                       LdapController ldapController) {
        this.authenticationController = authenticationController;
        this.currencyController = currencyController;
        this.publishingController = publishingController;
        this.ldapController = ldapController;
    }
}

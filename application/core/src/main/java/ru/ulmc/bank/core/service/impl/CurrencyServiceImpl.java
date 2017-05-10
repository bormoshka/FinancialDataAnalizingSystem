package ru.ulmc.bank.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ulmc.bank.dao.entity.financial.Currency;
import ru.ulmc.bank.dao.entity.financial.CurrencyAttributes;
import ru.ulmc.bank.dao.repository.CurrencyAttributesRepository;
import ru.ulmc.bank.dao.repository.CurrencyRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Сервис работы с валютой
 */
@Service
public class CurrencyServiceImpl {
    private final CurrencyRepository currencyRepository;
    private final CurrencyAttributesRepository currencyAttributesRepository;
    private final EntityManager entityManager;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               CurrencyAttributesRepository currencyAttributesRepository,
                               EntityManager entityManager) {
        this.currencyRepository = currencyRepository;
        this.currencyAttributesRepository = currencyAttributesRepository;
        this.entityManager = entityManager;
    }

    public Currency createCurrency(String code, String title, int scale) {
        Currency currency = new Currency(code, true);

        currencyRepository.save(currency);
        CurrencyAttributes attr = makeAttributes(currency, title, scale);
        currency.setAttributes(attr);
        currencyAttributesRepository.save(attr);
        flushIfTransactionOpen();
        return currency;
    }

    public Currency findCurrency(String code) {
        return currencyRepository.findByCode(code);
    }

    public void changeCurrencyAttributes(Long id, String title, int scale) {
        Currency currency = currencyRepository.findOne(id);
        changeCurrencyAttributes(currency, title, scale);
    }

    public void changeCurrencyAttributes(Currency currency, String title, int scale) {
        findAndDisableOldAttributes(currency.getId());
        CurrencyAttributes attr = makeAttributes(currency, title, scale);
        currencyAttributesRepository.save(attr);
        flushIfTransactionOpen();
    }

    public void changeCurrencyAttributes(Long id, int scale) {
        Currency currency = currencyRepository.findOne(id);
        CurrencyAttributes oldAttrs = currency.getAttributes();

        findAndDisableOldAttributes(currency.getId());

        CurrencyAttributes attr = makeAttributes(currency, oldAttrs.getTitle(), scale);
        currencyAttributesRepository.save(attr);
        flushIfTransactionOpen();
    }

    public void changeCurrencyAttributes(Currency currency, int scale) {
        CurrencyAttributes oldAttrs = currency.getAttributes();
        findAndDisableOldAttributes(currency.getId());
        CurrencyAttributes attr = makeAttributes(currency, oldAttrs.getTitle(), scale);
        currencyAttributesRepository.save(attr);
        flushIfTransactionOpen();
    }

    private void findAndDisableOldAttributes(Long currencyId) {
        Query query = entityManager.createQuery("UPDATE CurrencyAttributes c SET c.isActive = false" +
                " WHERE c.currency.id = " + currencyId);
        query.executeUpdate();
    }

    private CurrencyAttributes makeAttributes(Currency currency, String title, int scale) {
        return new CurrencyAttributes(currency, scale, title);
    }

    private void flushIfTransactionOpen() {
        if (entityManager.isOpen()) {
            entityManager.flush();
        }
    }

}

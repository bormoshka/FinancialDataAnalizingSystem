package ru.ulmc.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulmc.dao.entity.financial.Currency;

/**
 * Created by User on 26.03.2017.
 */
public interface CurrencyRepository extends PagingAndSortingRepository<Currency, Long> {
    Currency findByCode(String code);
}

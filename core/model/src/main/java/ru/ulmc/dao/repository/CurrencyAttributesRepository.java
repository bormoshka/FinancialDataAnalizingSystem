package ru.ulmc.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulmc.dao.entity.financial.CurrencyAttributes;

/**
 * Created by User on 26.03.2017.
 */
public interface CurrencyAttributesRepository extends PagingAndSortingRepository<CurrencyAttributes, Long> {
}

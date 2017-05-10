package ru.ulmc.bank.dao.entity.financial;

import lombok.Data;
import ru.ulmc.bank.dao.entity.directory.CourseRange;

import javax.persistence.*;

/**
 * Валютная пара
 */
@Data
@Entity
@Table(name = "FIN_CURRENCY_PAIR",
        indexes = {
                @Index(name = "CURRENCY_PAIR_ID_INDEX", columnList = "ID", unique = true),
                @Index(name = "CURRENCY_PAIR_CURRENCIES_IDS_INDEX", columnList = "BASE_CURRENCY_ID, QUOTED_CURRENCY_ID",
                        unique = true)})
@SequenceGenerator(name = "SEQ_CURRENCY_PAIR")
public class CurrencyPair {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID", name = "BASE_CURRENCY_ID")
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID", name = "QUOTED_CURRENCY_ID")
    private Currency quotedCurrency;

    @Column(name = "BID", nullable = false)
    private Double bid;

    @Column(name = "ASK", nullable = false)
    private Double ask;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID", name = "COURSE_RANGE_ID")
    private CourseRange range;

}

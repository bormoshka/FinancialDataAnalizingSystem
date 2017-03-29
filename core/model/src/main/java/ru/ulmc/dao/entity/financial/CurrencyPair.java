package ru.ulmc.dao.entity.financial;

import javax.persistence.*;

/**
 * Валютная пара
 */
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

    @Column (name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getQuotedCurrency() {
        return quotedCurrency;
    }

    public void setQuotedCurrency(Currency quotedCurrency) {
        this.quotedCurrency = quotedCurrency;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyPair that = (CurrencyPair) o;

        if (!id.equals(that.id)) return false;
        if (!baseCurrency.equals(that.baseCurrency)) return false;
        return quotedCurrency.equals(that.quotedCurrency);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + baseCurrency.hashCode();
        result = 31 * result + quotedCurrency.hashCode();
        return result;
    }
}

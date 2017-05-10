package ru.ulmc.bank.dao.entity.financial;

import lombok.Data;
import ru.ulmc.bank.dao.entity.system.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Атрибуты валюты.
 * Хранятся все атрибуты& последняя запись является актуальной.
 * Храним историю атрибутов
 */
@Data
@Entity
@Table(name = "FIN_CURRENCY_ATTRIBUTES")
@SequenceGenerator(name = "SEQ_CURRENCY_ATTRIBUTES", allocationSize = 1)
public class CurrencyAttributes {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "scale", nullable = false)
    private Integer scale;

    @Column(name = "title")
    private String title;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID", name = "CURRENCY_ID")
    private Currency currency;

    @Column(name = "is_current", nullable = false)
    private boolean isCurrent;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID", name = "CREATOR_ID")
    private User creator;

    public CurrencyAttributes() {
    }

    public CurrencyAttributes(Currency currency, Integer scale, String title) {
        this.scale = scale;
        this.title = title;
        this.currency = currency;
        this.isCurrent = true;
        this.createDate = new Date();
    }
}

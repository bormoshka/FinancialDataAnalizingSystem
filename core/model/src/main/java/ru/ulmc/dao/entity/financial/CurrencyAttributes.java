package ru.ulmc.dao.entity.financial;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Атрибуты валюты
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

    @Column(name = "active", nullable = false)
    private boolean isActive;

    public CurrencyAttributes() {
    }

    public CurrencyAttributes(Currency currency, Integer scale, String title) {
        this.scale = scale;
        this.title = title;
        this.currency = currency;
        this.isActive = true;
        this.createDate = new Date();
    }
}

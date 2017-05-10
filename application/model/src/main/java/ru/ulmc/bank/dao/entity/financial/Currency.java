package ru.ulmc.bank.dao.entity.financial;

import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Валюта. Неизменяемая сущность.
 */
@Entity
@Table(name = "FIN_CURRENCY",
        indexes = {@Index(name = "currency_code_index", columnList = "code", unique = true)})
@SequenceGenerator(name = "SEQ_CURRENCY", allocationSize = 1)
public class Currency {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    @Getter
    private String code;

    @Column(name = "is_active", nullable = false)
    @Getter
    private boolean isActive;

    @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
    @Where(clause = "is_current = true")
    @OrderBy("createDate DESC")
    private List<CurrencyAttributes> attributes;

    public Currency() {
    }

    public Currency(String code, boolean isActive) {
        this.code = code;
        this.isActive = isActive;
    }

    public CurrencyAttributes getAttributes() {
        if (attributes != null)
            return attributes.get(0);
        else
            return null;
    }

    public void setAttributes(CurrencyAttributes attribute) {
        if (this.attributes == null || !this.attributes.isEmpty())
            this.attributes = new ArrayList<>();
        this.attributes.add(0, attribute);
    }

}

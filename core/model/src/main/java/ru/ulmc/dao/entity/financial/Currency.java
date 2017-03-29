package ru.ulmc.dao.entity.financial;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Валюта
 */
@Data
@Entity
@Table(name = "FIN_CURRENCY",
        indexes = {@Index(name = "currency_code_index", columnList = "code", unique = true)})
@SequenceGenerator(name = "SEQ_CURRENCY", allocationSize = 1)
public class Currency {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
    @Where(clause = "active = true")
    @OrderBy("createDate DESC")
    private List<CurrencyAttributes> attributes;

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

package ru.ulmc.bank.dao.entity.organization;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import ru.ulmc.bank.dao.entity.financial.Currency;

/**
 * Подразделение.
 */
@Data
@Entity
@Table(name = "ORG_DEPARTMENT",
        indexes = {@Index(name = "DEPARTMENT_ID_INDEX", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_DEPARTMENT")
public class Department {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "ID", name = "PARENT_DEPARTMENT_ID")
    protected Department parent;

    @Column(name = "DEPARTMENT_NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    //@ManyToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "DEPARTMENT_ID")
    //private Collection<DepartmentContactEntry> contacts;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

}

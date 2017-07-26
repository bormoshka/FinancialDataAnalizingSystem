package ru.ulmc.bank.dao.entity.organization;

import lombok.Data;
import ru.ulmc.bank.dao.entity.financial.Currency;

import javax.persistence.*;
import java.util.Collection;

/**
 * Вид курса.
 */
@Data
@Entity
@Table(name = "ORG_COURSE_KIND",
        indexes = {@Index(name = "COURSE_KIND_ID_INDEX", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_COURSE_KIND")
public class CourseKind {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ORG_COURSE_KIND_TO_FIN_CURRENCY", joinColumns = {
            @JoinColumn(name = "COURSE_KIND_ID")}, inverseJoinColumns = {@JoinColumn(name = "CURRENCY_ID")})
    private Collection<Currency> currencies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "ID", name = "PARENT_COURSE_KIND")
    protected CourseKind parent;

    @Column(name = "CK_NAME")
    private String name;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

}

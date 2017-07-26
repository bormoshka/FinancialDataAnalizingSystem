package ru.ulmc.bank.dao.entity.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * Контактные данные.
 */
@Data
@Entity
@Table(name = "ORG_CONTACT",
        indexes = {@Index(name = "CONTACT_ID_INDEX", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_CONTACT")
public class Contact {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CONTACT_VALUE")
    private String value;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @Column(name = "CONTACT_NAME", nullable = true)
    private String name;

    @Column(name = "CONTACT_TYPE")
    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}

package ru.ulmc.bank.dao.entity.organization;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * Контактные данные.
 */
@Data
//@Entity
//@Table(name = "ORG_CONTACT",
//        indexes = {@Index(name = "CONTACT_ID_INDEX",
//                columnList = "CONTACT_ID, DEPARTMENT_ID", unique = true)})
public class DepartmentContactEntry {
    @Id
    @Embedded
    private DepartmentContactKey key;

    @Column(name = "CONTACT_SUB_TYPE")
    @Enumerated(EnumType.STRING)
    private ContactEntryType type;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Data
    @Embeddable
    private static class DepartmentContactKey {
        @Column(name = "CONTACT_ID")
        private Long contactId;

        @Column(name = "DEPARTMENT_ID")
        private Long departmentId;
    }
}

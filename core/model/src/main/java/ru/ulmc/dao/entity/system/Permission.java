package ru.ulmc.dao.entity.system;

import lombok.Data;

import javax.persistence.*;

/**
 * Разрешение/права на выполнение действий.
 */
@Data
@Entity
@Table(name = "SYS_PERMISSION",
        indexes = {@Index(name = "PERMISSION_ID_INDX", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_PERMISSION", allocationSize = 1)
public class Permission {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    private Permission parent;

    @Column(name = "CODE", nullable = false)
    private String code;

    public Permission(String code) {
        this.code = code;
    }
}

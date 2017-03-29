package ru.ulmc.dao.entity.system;

import javax.persistence.*;

/**
 * Разрешение/права на выполнение действий.
 */
@Entity
@Table(name = "SYS_PERMISSION",
        indexes = {@Index(name = "PERMISSION_ID_INDX", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_PERMISSION", allocationSize = 1)
public class Permission {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne( optional = true)
    private Permission parent;

    @Column(name = "CODE", nullable = false)
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getParent() {
        return parent;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

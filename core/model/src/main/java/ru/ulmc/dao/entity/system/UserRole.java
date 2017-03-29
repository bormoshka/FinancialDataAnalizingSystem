package ru.ulmc.dao.entity.system;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * Разрешение/права на выполнение действий.
 */
@Data
@Entity
@Table(name = "SYS_PERMISSION",
        indexes = {@Index(name = "PERMISSION_ID_INDX", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_PERMISSION", allocationSize = 1)
public class UserRole {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "SYS_ROLES_PERMISSIONS",
            joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID"))
    private Collection<Permission> permissions;

    public UserRole(String name) {
        this.name = name;
    }
}

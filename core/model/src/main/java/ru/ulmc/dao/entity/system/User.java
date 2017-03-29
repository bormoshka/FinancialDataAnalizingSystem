package ru.ulmc.dao.entity.system;

import javax.persistence.*;
import java.util.Set;

/**
 * Пользователь системы
 */
@Entity
@Table(name = "SYS_USER",
        indexes = {@Index(name = "USER_ID_LOGIN", columnList = "id, login", unique = true)})
@SequenceGenerator(name = "SEQ_USER", allocationSize = 1)
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "FULLNAME")
    private String fullName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USER_TO_PERMISSION",
            joinColumns = {@JoinColumn(name = "USER_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "PERMISSION_ID", nullable = false, updatable = false)})
    private Set<Permission> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}

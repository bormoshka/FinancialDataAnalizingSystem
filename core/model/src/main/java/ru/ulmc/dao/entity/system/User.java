package ru.ulmc.dao.entity.system;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * Пользователь системы
 */
@Data
@Entity
@Table(name = "SYS_USER",
        indexes = {@Index(name = "USER_ID_LOGIN", columnList = "id, login", unique = true)})
@SequenceGenerator(name = "SEQ_USER", allocationSize = 1)
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "FULLNAME", nullable = false)
    private String fullName;

    @Column(name = "IS_ENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "IS_TOKEN_EXPIRED")
    private boolean tokenExpired;

    @ManyToMany
    @JoinTable(
            name = "SYS_USERS_ROLES",
            joinColumns = @JoinColumn(
                    name = "USER_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "ROLE_ID", referencedColumnName = "id"))
    private Collection<UserRole> roles;

}

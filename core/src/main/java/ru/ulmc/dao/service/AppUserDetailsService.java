package ru.ulmc.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulmc.common.Roles;
import ru.ulmc.dao.entity.system.Permission;
import ru.ulmc.dao.entity.system.User;
import ru.ulmc.dao.entity.system.UserRole;
import ru.ulmc.dao.repository.UserRepository;
import ru.ulmc.dao.repository.UserRoleRepository;

import java.util.*;

/**
 * Сервис поиска и конвертации пользователей приложения в ползователей Spring
 */
@Service
@Transactional
public class AppUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userRepository.findByLogin(login);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Collections.singletonList(roleRepository.findByName(Roles.ROLE_EMPTY.name()))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<UserRole> roles) {

        return getGrantedAuthorities(getPermissions(roles));
    }

    private List<String> getPermissions(Collection<UserRole> roles) {
        List<String> perms = new ArrayList<>();
        List<Permission> collection = new ArrayList<>();
        for (UserRole role : roles) {
            collection.addAll(role.getPermissions());
        }
        for (Permission item : collection) {
            perms.add(item.getCode());
        }
        return perms;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
package ru.ulmc.bank.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.core.service.UserService;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.dao.repository.UserRepository;
import ru.ulmc.bank.dao.repository.UserRoleRepository;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public User getUserByLoginAndEncodedPassword(String login, String encodedPassword) throws AuthenticationException {
        try {
            User user = userRepository.findByLoginAndPassword(login, encodedPassword);
            if (user == null) {
                throw new AuthenticationException();
            }
            return user;
        } catch (Exception ex) {
            throw new AuthenticationException(ex);
        }
    }
}

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulmc.bank.core.common.Roles;
import ru.ulmc.bank.dao.entity.system.Permission;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.dao.entity.system.UserRole;
import ru.ulmc.bank.dao.repository.UserRoleRepository;
import ru.ulmc.bank.dao.repository.UserRepository;

import java.util.*;


// Сервис поиска и конвертации пользователей приложения в ползователей Spring

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails getUserByLoginAndPassword(String login) throws UsernameNotFoundException {

        User user = userRepository.findByLogin(login);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Collections.singletonList(roleRepository.findByName(Roles.EMPTY))));
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
            perms.add(item.getName());
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

}*/
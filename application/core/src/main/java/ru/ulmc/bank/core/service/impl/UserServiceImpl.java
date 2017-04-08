package ru.ulmc.bank.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulmc.bank.core.common.security.UserPrincipal;
import ru.ulmc.bank.dao.entity.system.Permission;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.dao.entity.system.UserRole;
import ru.ulmc.bank.dao.repository.UserRepository;
import ru.ulmc.bank.dao.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("Не найден пользователь с именем " + login);
        }
        return new UserPrincipal(user);
    }

    public User findUser(String login) {
        return userRepository.findByLogin(login);
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
}
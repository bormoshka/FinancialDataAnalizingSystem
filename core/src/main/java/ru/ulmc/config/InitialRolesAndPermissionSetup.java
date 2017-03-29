package ru.ulmc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ulmc.common.Perms;
import ru.ulmc.common.Roles;
import ru.ulmc.dao.entity.system.Permission;
import ru.ulmc.dao.entity.system.User;
import ru.ulmc.dao.entity.system.UserRole;
import ru.ulmc.dao.repository.PermissionRepository;
import ru.ulmc.dao.repository.UserRepository;
import ru.ulmc.dao.repository.UserRoleRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Класс, отвечающий за инициализацию пользовательский холей и разрешений.
 */
@Component
public class InitialRolesAndPermissionSetup implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    @Autowired
    public InitialRolesAndPermissionSetup(UserRepository userRepository,
                                          UserRoleRepository roleRepository,
                                          PermissionRepository permissionRepository,
                                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        Permission finCurrencyRead = createPermissionIfNotFound(Perms.FIN_CURRENCY_READ.name());
        Permission finCurrencyWrite = createPermissionIfNotFound(Perms.FIN_CURRENCY_WRITE.name());
        Permission sysUserCreate = createPermissionIfNotFound(Perms.SYS_USER_CREATE.name());
        Permission sysUserEdit = createPermissionIfNotFound(Perms.SYS_USER_EDIT.name());
        Permission sysUserRead = createPermissionIfNotFound(Perms.SYS_USER_READ.name());

        List<Permission> adminPermissions = Arrays.asList(sysUserCreate, sysUserEdit, sysUserRead);
        List<Permission> managerPermissions = Arrays.asList(finCurrencyRead, finCurrencyWrite, sysUserRead);
        List<Permission> auditorPermissions = Arrays.asList(finCurrencyRead, sysUserRead);
        List<Permission> marketingPermissions = Arrays.asList(finCurrencyRead, sysUserRead);

        createRoleIfNotFound(Roles.ROLE_EMPTY.name(), Collections.emptyList());
        createRoleIfNotFound(Roles.ROLE_ADMIN.name(), adminPermissions);
        createRoleIfNotFound(Roles.ROLE_AUDITOR.name(), auditorPermissions);
        createRoleIfNotFound(Roles.ROLE_MANAGER.name(), managerPermissions);
        createRoleIfNotFound(Roles.ROLE_MARKETING.name(), marketingPermissions);

        UserRole adminRole = roleRepository.findByName(Roles.ROLE_ADMIN.name());
        User user = new User();
        user.setLogin("admin");
        user.setFullName("System Admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    private Permission createPermissionIfNotFound(String name) {
        Permission permission = permissionRepository.findByCode(name);
        if (permission == null) {
            permission = new Permission(name);
            permissionRepository.save(permission);
        }
        return permission;
    }

    @Transactional
    private UserRole createRoleIfNotFound(String name, Collection<Permission> permissions) {
        UserRole role = roleRepository.findByName(name);
        if (role == null) {
            role = new UserRole(name);
            role.setPermissions(permissions);
            roleRepository.save(role);
        }
        return role;
    }
}

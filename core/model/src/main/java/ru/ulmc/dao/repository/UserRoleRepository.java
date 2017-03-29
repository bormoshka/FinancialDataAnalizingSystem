package ru.ulmc.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulmc.dao.entity.system.UserRole;

public interface UserRoleRepository extends PagingAndSortingRepository<UserRole, Long> {
    UserRole findByName(String name);
}

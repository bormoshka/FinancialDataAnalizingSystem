package ru.ulmc.bank.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulmc.bank.dao.entity.system.Permission;

public interface PermissionRepository extends PagingAndSortingRepository<Permission, Long> {
    Permission findByName(String code);
}

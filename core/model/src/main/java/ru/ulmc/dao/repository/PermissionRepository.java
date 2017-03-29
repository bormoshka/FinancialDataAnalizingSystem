package ru.ulmc.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulmc.dao.entity.system.Permission;

public interface PermissionRepository extends PagingAndSortingRepository<Permission, Long> {
    Permission findByCode(String code);
}

package ru.ulmc.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulmc.dao.entity.system.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByLogin(String login);

    User findByFullNameAndLogin(String fullname, String login);
}

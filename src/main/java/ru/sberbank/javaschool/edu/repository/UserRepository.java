package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
    User findUserByActcode(String code);

    User findUserById(Long id);

}
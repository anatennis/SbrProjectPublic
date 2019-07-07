package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.sberbank.javaschool.edu.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

}
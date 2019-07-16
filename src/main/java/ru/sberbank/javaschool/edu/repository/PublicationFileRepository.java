package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.javaschool.edu.domain.PublicationFile;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;

import java.util.List;

@Repository
public interface PublicationFileRepository extends JpaRepository<PublicationFile, Long> {
    List<PublicationFile> findAllByUserAndTask(User user, Task task);

}

package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.domain.UserTask;

import java.util.List;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    UserTask findUserTaskByUserAndTask(User user, Task task);

    List<UserTask> findUserTaskByTask(Task task);
}

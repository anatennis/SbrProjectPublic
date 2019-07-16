package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    TaskComment findTaskCommentById(long id);
}

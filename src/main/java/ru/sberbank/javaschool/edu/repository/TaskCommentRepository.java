package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sberbank.javaschool.edu.domain.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    TaskComment findTaskCommentById(long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TaskComment comment "
            + "SET comment.text=:newText "
            + "WHERE comment.id=:id")
    void updateTaskComent(
            @Param("id") long idComment,
            @Param("newText") String newText
    );
}

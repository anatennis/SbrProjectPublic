package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task getTaskByCourseAndAuthorAndTitle(Course course, User author, String title);

    Task getTaskById(long id);

    List<Task> getTaskByCourseOrderById(Course course);

    List<Task> getTaskByCourse(Course course);
}

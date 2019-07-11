package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.User;

public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {
    CourseUser findCourseUserByCourseAndUser(Course course, User user);
}

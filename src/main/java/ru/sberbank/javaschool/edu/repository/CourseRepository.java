package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findCourseByCaption(String caption);
    Course findCourseById(Long id);
}

package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.User;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material getMaterialByCourseAndAuthorAndTitle(Course course, User author, String title);

    Material getMaterialById(long id);

    @Query(
            value = "SELECT p.* FROM edu_publication p WHERE p.course = :course_id " +
                    "AND p.dtype = 'Material' ORDER BY id ASC",
            nativeQuery = true
    )
    List<Material> getMaterialByCourseOrderById(@Param("course_id") long courseId);

}

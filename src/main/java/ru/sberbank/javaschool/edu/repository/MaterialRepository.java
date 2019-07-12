package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.User;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material getMaterialByCourseAndAuthorAndTitle(Course course, User author, String title);

    Material getMaterialById(long id);

    List<Material> getMaterialByCourseOrderById(Course course);

}

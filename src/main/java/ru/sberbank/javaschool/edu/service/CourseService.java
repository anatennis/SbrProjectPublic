package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.repository.CourseRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseUserService courseUserService;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            CourseUserService courseUserService
    ) {
        this.courseRepository = courseRepository;
        this.courseUserService = courseUserService;
    }

    @Transactional
    public boolean addCourse(Course course) {
        Course courseFromDB = courseRepository.findCourseByCaption(course.getCaption());

        if (courseFromDB != null) {
            return false;
        }

        course.setCreateDate(LocalDateTime.now());

        courseRepository.save(course);
        courseUserService.addAdminToAllCourses(course);

        return true;
    }

    @Transactional
    public void removeCourse(Long id) {

        courseRepository.deleteById(id);
    }

    @Transactional
    public void updateCourse(Long id, String newCaption) {

        courseRepository.updateCourse(id, newCaption);
    }

    public Course findCourseById(long id) {

        return courseRepository.findCourseById(id);
    }

    public List<Course> findAll() {

        return courseRepository.findAll();
    }

    public boolean hasAccess(Principal principal) {

        return principal.getName().equals("admin");
    }

}

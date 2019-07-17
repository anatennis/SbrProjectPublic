package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public boolean removeCourse(Long id) {
        Course courseFromDb = courseRepository.findCourseById(id);

        if (courseFromDb == null) {
            return false;
        }

        courseRepository.delete(courseFromDb);

        return true;
    }

    public boolean updateCourse(Long id, String newCaption) {
        Course courseFromDb = courseRepository.findCourseById(id);

        if (courseFromDb == null) {
            return false;
        }

        courseFromDb.setCaption(newCaption);

        courseRepository.save(courseFromDb);

        return true;
    }

    public Course findCourseById(long id) {

        return courseRepository.findCourseById(id);
    }

    public List<Course> findAll() {

        return courseRepository.findAll();
    }

    public boolean hasAccess(Principal principal) {  //костыльненькое ограничение прав доступа на админку

        return principal.getName().equals("admin");
    }

}

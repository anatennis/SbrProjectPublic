package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            UserRepository userRepository
    ) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getAllUserCourses(String login) {
        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    public boolean addCourse(Course course) {
        Course courseFromDB = courseRepository.findCourseByCaption(course.getCaption());

        if (courseFromDB != null) {
            return false;
        }

        course.setCreateDate(LocalDateTime.now());

        courseRepository.save(course);

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

}
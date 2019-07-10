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

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllUserCourses(String login) {
        //List<Course> courses = courseRepository.findAllCoursesByUserLogin(login);
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
        Course courseFromDB = courseRepository.findCourseById(id);

        if (courseFromDB == null) {
            return false;
        }

        courseRepository.delete(courseFromDB);

        return true;
    }

    public boolean updateCourse(Long id, String newCaption) {
        Course courseFromDB = courseRepository.findCourseById(id);

        if (courseFromDB == null) {
            return false;
        }

        courseFromDB.setCaption(newCaption);

        courseRepository.save(courseFromDB);

        return true;
    }

}

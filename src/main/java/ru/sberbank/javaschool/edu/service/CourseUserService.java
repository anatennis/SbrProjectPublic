package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.Role;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.CourseUserRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseUserService {
    private final CourseUserRepository courseUserRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseUserService(
            CourseUserRepository courseUserRepository,
            CourseRepository courseRepository,
            UserRepository userRepository
    ) {
        this.courseUserRepository = courseUserRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public boolean addCourseUser(Long courseId, String userLogin, String role) {
        Course course = courseRepository.findCourseById(courseId);
        User user = userRepository.findUserByLogin(userLogin);
        Role userRole = Role.valueOf(role);

        CourseUser courseUserFromDb = courseUserRepository.findCourseUserByCourseAndUser(course, user);

        if (courseUserFromDb != null) {
            return false;
        }

        CourseUser courseUser = new CourseUser(course, user, userRole);
        courseUserRepository.save(courseUser);

        return true;
    }

    public List<CourseUser> getUserCourses(String login) {
        User user = userRepository.findUserByLogin(login);
        List<CourseUser> courses = courseUserRepository.findCourseUserByUser(user);

        return courses;
    }

    public List<CourseUser> getCourseUsersWithoutTeachers(Course course) {
        List<CourseUser> courseUsersWithoutTeachers = courseUserRepository.findCourseUserByCourse(course)
                .stream()
                .filter(u -> u.getRole() != Role.TEACHER)
                .collect(Collectors.toList());

        return courseUsersWithoutTeachers;
    }

    public boolean isTeacher(User user, Course course) {
        CourseUser courseUser = courseUserRepository.findCourseUserByCourseAndUser(course, user);
        if (courseUser.getRole() != Role.TEACHER) {
            return false;
        }
        return true;
    }

    public void deleteCourseUser(long id) {

        courseUserRepository.deleteById(id);
    }

    //Для автоматического назначения админа на все курсы как учителя для возможности редактирования
    public void addAdminToAllCourses(Course course) {
        User admin = userRepository.findUserByLogin("admin");
        CourseUser courseUserAdmin = new CourseUser(course, admin, Role.TEACHER);
        courseUserRepository.save(courseUserAdmin);

    }
}

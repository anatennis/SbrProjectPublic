package ru.sberbank.javaschool.edu.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.Role;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.CourseUserRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseUserServiceTest {
    @Autowired
    private CourseUserService courseUserService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseUserRepository courseUserRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    public void addCourseUser() {
        User user = new User();
        Course course = new Course();

        user.setId(1L);
        user.setLogin("test");

        course.setId(1L);

        CourseUser courseUser = new CourseUser(course, user, Role.TEACHER);

        Mockito.doReturn(user)
                .when(userRepository)
                .findUserByLogin("test");
        Mockito.doReturn(course)
                .when(courseRepository)
                .findCourseById(1L);

        boolean isAdded = courseUserService.addCourseUser(1L,"test", "TEACHER");

        Assert.assertTrue(isAdded);

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin("test");
        Mockito.verify(courseUserRepository, Mockito.times(1))
                .findCourseUserByCourseAndUser(course, user);
        Mockito.verify(courseUserRepository, Mockito.times(1)).save(courseUser);

    }

    @Test
    public void addCourseUserWhenSameExist() {
        User user = new User();
        Course course = new Course();

        user.setId(1L);
        user.setLogin("test");

        course.setId(1L);

        CourseUser courseUser = new CourseUser(course, user, Role.TEACHER);

        Mockito.doReturn(user)
                .when(userRepository)
                .findUserByLogin("test");
        Mockito.doReturn(course)
                .when(courseRepository)
                .findCourseById(1L);
        Mockito.doReturn(courseUser)
                .when(courseUserRepository)
                .findCourseUserByCourseAndUser(course, user);

        boolean isAdded = courseUserService.addCourseUser(1L,"test", "TEACHER");

        Assert.assertFalse(isAdded);

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin("test");
        Mockito.verify(courseUserRepository, Mockito.times(1))
                .findCourseUserByCourseAndUser(course, user);
        Mockito.verify(courseUserRepository, Mockito.times(0)).save(courseUser);

    }

    @Test
    public void deleteCourseUser() {
        CourseUser courseUser = new CourseUser();

        courseUser.setId(1L);

        Mockito.doReturn(courseUser)
                .when(courseUserRepository)
                .findCourseUserById(1L);

        boolean isDeleted = courseUserService.deleteCourseUser(1L);

        Assert.assertTrue(isDeleted);

        Mockito.verify(courseUserRepository, Mockito.times(1))
                .findCourseUserById(1L);
        Mockito.verify(courseUserRepository, Mockito.times(1)).delete(courseUser);
    }

    @Test
    public void deleteNonexistentCourseUser() {
        CourseUser courseUser = new CourseUser();

        courseUser.setId(1L);

        Mockito.doReturn(null)
                .when(courseUserRepository)
                .findCourseUserById(1L);

        boolean isDeleted = courseUserService.deleteCourseUser(1L);

        Assert.assertFalse(isDeleted);

        Mockito.verify(courseUserRepository, Mockito.times(1))
                .findCourseUserById(1L);
        Mockito.verify(courseUserRepository, Mockito.times(0)).delete(courseUser);
    }

    @Test
    public void isTeacher() {
        User user = new User();
        Course course = new Course();

        CourseUser courseUser = new CourseUser(course, user, Role.TEACHER);

        Mockito.doReturn(courseUser)
                .when(courseUserRepository)
                .findCourseUserByCourseAndUser(course, user);

        Assert.assertTrue(courseUserService.isTeacher(user, course));
    }
}
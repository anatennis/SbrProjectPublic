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
import ru.sberbank.javaschool.edu.repository.CourseRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseUserService courseUserService;

    @Test
    public void addCourse() {
        Course course = new Course();
        course.setCaption("Test");

        boolean isCreated = courseService.addCourse(course);

        Assert.assertTrue(isCreated);
        Assert.assertNotNull(course.getCreateDate());

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseByCaption("Test");
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
        Mockito.verify(courseUserService, Mockito.times(1)).addAdminToAllCourses(course);

    }

    @Test
    public void addCourseAlreadyExist(){
        Course course = new Course();

        course.setCaption("Test");

        Mockito.doReturn(new Course())
                .when(courseRepository)
                .findCourseByCaption("Test");

        boolean isCreated = courseService.addCourse(course);

        Assert.assertFalse(isCreated);

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseByCaption("Test");
        Mockito.verify(courseRepository, Mockito.times(0)).save(course);
        Mockito.verify(courseUserService, Mockito.times(0)).addAdminToAllCourses(course);
    }

    @Test
    public void removeCourse() {
        Course course = new Course();
        long id = 1L;

        course.setId(id);

        Mockito.doReturn(course)
                .when(courseRepository)
                .findCourseById(id);

        boolean isRemoved = courseService.removeCourse(id);

        Assert.assertTrue(isRemoved);

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(id);
        Mockito.verify(courseRepository, Mockito.times(1)).delete(course);

    }

    @Test
    public void removeCourseThatDothNotExist() {
        Course course = new Course();
        long id = 1L;

        Mockito.doReturn(null)
                .when(courseRepository)
                .findCourseById(id);

        boolean isRemoved = courseService.removeCourse(id);

        Assert.assertFalse(isRemoved);

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(id);
        Mockito.verify(courseRepository, Mockito.times(0)).delete(course);

    }

    @Test
    public void updateCourse() {
        Course course = new Course();
        long id = 1L;

        course.setId(id);
        course.setCaption("old");

        Mockito.doReturn(course)
                .when(courseRepository)
                .findCourseById(id);

        boolean isUpdated = courseService.updateCourse(id, "new");

        Assert.assertTrue(isUpdated);
        Assert.assertEquals("new", course.getCaption());

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(id);
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
    }

    @Test
    public void updateCourseThatDothNotExist() {
        Course course = new Course();
        long id = 1L;

        course.setId(id);
        course.setCaption("old");

        Mockito.doReturn(null)
                .when(courseRepository)
                .findCourseById(id);

        boolean isUpdated = courseService.updateCourse(id, "new");

        Assert.assertFalse(isUpdated);
        Assert.assertEquals("old", course.getCaption());

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(id);
        Mockito.verify(courseRepository, Mockito.times(0)).save(course);
    }

}
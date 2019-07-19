package ru.sberbank.javaschool.edu.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaterialServiceTest {
    @Autowired
    private MaterialService materialService;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TaskRepository taskRepository;

    private final long ID = 1L;



    @Test
    public void createMaterial() {
        Material material = new Material();
        User user = new User();
        Course course = new Course();

        material.setTitle("test");
        user.setId(ID);
        course.setId(ID);

        boolean isCreated = materialService.createMaterial(course, user, material);

        Assert.assertTrue(isCreated);
        Assert.assertEquals(course, material.getCourse());
        Assert.assertEquals(user, material.getAuthor());
        Assert.assertNotNull(material.getCreateDate());

        Mockito.verify(materialRepository, Mockito.times(1))
                .getMaterialByCourseAndAuthorAndTitle(course, user, "test");
        Mockito.verify(materialRepository, Mockito.times(1)).save(material);
    }

    @Test
    public void createExistingMaterial() {
        Material material = new Material();
        User user = new User();
        Course course = new Course();

        material.setTitle("test");
        user.setId(ID);
        course.setId(ID);

        Mockito.doReturn(new Material())
                .when(materialRepository)
                .getMaterialByCourseAndAuthorAndTitle(course, user, "test");

        boolean isCreated = materialService.createMaterial(course, user, material);

        Assert.assertFalse(isCreated);

        Mockito.verify(materialRepository, Mockito.times(1))
                .getMaterialByCourseAndAuthorAndTitle(course, user, "test");
        Mockito.verify(materialRepository, Mockito.times(0)).save(material);
    }

    @Test
    public void editMaterial() {
        Material materialOld = new Material();
        Material material = new Material();
        long ID = 1L;

        materialOld.setId(ID);
        materialOld.setTitle("old");
        materialOld.setText("oldText");

        material.setTitle("new");
        material.setText("newText");

        Mockito.doReturn(materialOld)
                .when(materialRepository)
                .getMaterialById(ID);

        boolean isEdited = materialService.editMaterial(ID, material);

        Assert.assertTrue(isEdited);
        Assert.assertEquals("new", materialOld.getTitle());
        Assert.assertEquals("newText", materialOld.getText());

        Mockito.verify(materialRepository, Mockito.times(1)).getMaterialById(ID);
        Mockito.verify(materialRepository, Mockito.times(1)).save(materialOld);
    }

    @Test
    public void editNonexistentMaterial() {
        Material materialOld = new Material();
        Material material = new Material();

        materialOld.setId(ID);

        Mockito.doReturn(null)
                .when(materialRepository)
                .getMaterialById(ID);

        boolean isEdited = materialService.editMaterial(ID, material);

        Assert.assertFalse(isEdited);

        Mockito.verify(materialRepository, Mockito.times(1)).getMaterialById(ID);
        Mockito.verify(materialRepository, Mockito.times(0)).save(materialOld);
    }

    @Test
    public void createTask() {
        Task task = new Task();
        User user = new User();
        Course course = new Course();

        task.setTitle("test");
        user.setId(ID);
        course.setId(ID);

        boolean isCreated = materialService.createTask(course, user, task);

        Assert.assertTrue(isCreated);
        Assert.assertEquals(course, task.getCourse());
        Assert.assertEquals(user, task.getAuthor());
        Assert.assertNotNull(task.getCreateDate());

        Mockito.verify(taskRepository, Mockito.times(1))
                .getTaskByCourseAndAuthorAndTitle(course, user, "test");
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void createExistingTask() {
        Task task = new Task();
        User user = new User();
        Course course = new Course();

        task.setTitle("test");
        user.setId(ID);
        course.setId(ID);

        Mockito.doReturn(new Task())
                .when(taskRepository)
                .getTaskByCourseAndAuthorAndTitle(course, user, "test");

        boolean isCreated = materialService.createTask(course, user, task);

        Assert.assertFalse(isCreated);

        Mockito.verify(taskRepository, Mockito.times(1))
                .getTaskByCourseAndAuthorAndTitle(course, user, "test");
        Mockito.verify(taskRepository, Mockito.times(0)).save(task);
    }

    @Test
    public void editTask() {
        Task taskOld = new Task();
        Task taskNew = new Task();

        taskOld.setId(ID);
        taskOld.setTitle("old");
        taskOld.setText("oldText");
        taskOld.setMaxMark(100L);
        taskOld.setCompleteTime(LocalDateTime.of(2019,8,12,20,0));

        taskNew.setTitle("new");
        taskNew.setText("newText");
        taskNew.setMaxMark(200L);
        taskNew.setCompleteTime(LocalDateTime.of(2019,8,15,20,0));

        Mockito.doReturn(taskOld)
                .when(taskRepository)
                .getTaskById(ID);

        boolean isEdited = materialService.editTask(ID, taskNew);

        Assert.assertTrue(isEdited);
        Assert.assertEquals(taskNew.getTitle(), taskOld.getTitle());
        Assert.assertEquals(taskNew.getText(), taskOld.getText());
        Assert.assertEquals(taskNew.getMaxMark(), taskOld.getMaxMark());
        Assert.assertEquals(taskNew.getCompleteTime(), taskOld.getCompleteTime());

        Mockito.verify(taskRepository, Mockito.times(1)).getTaskById(ID);
        Mockito.verify(taskRepository, Mockito.times(1)).save(taskOld);
    }

    @Test
    public void editNonexistentTask() {
        Task taskOld = new Task();
        Task taskNew = new Task();

        taskOld.setId(ID);

        Mockito.doReturn(null)
                .when(taskRepository)
                .getTaskById(ID);

        boolean isEdited = materialService.editTask(ID, taskNew);

        Assert.assertFalse(isEdited);

        Mockito.verify(taskRepository, Mockito.times(1)).getTaskById(ID);
        Mockito.verify(taskRepository, Mockito.times(0)).save(taskOld);
    }

    @Test
    public void deleteMaterial() {
        Material material = new Material();
        User user = new User();
        Course course = new Course();

        material.setId(ID);
        user.setId(ID);
        course.setId(ID);
        course.setCourseUsers(new HashSet<>(Arrays.asList(new CourseUser(course, user, Role.TEACHER))));

        Mockito.doReturn(course)
                .when(courseRepository)
                .findCourseById(ID);
        Mockito.doReturn(material)
                .when(materialRepository)
                .getMaterialById(ID);

        Assert.assertTrue(materialService.deletePublication(ID, ID, user, true));

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(ID);
        Mockito.verify(materialRepository, Mockito.times(1)).getMaterialById(ID);
        Mockito.verify(materialRepository, Mockito.times(1)).delete(material);
    }

    @Test
    public void deleteTask() {
        Task task = new Task();
        User user = new User();
        Course course = new Course();

        task.setId(ID);
        user.setId(ID);
        course.setId(ID);
        course.setCourseUsers(new HashSet<>(Collections.singletonList(new CourseUser(course, user, Role.TEACHER))));

        Mockito.doReturn(course)
                .when(courseRepository)
                .findCourseById(ID);
        Mockito.doReturn(task)
                .when(taskRepository)
                .getTaskById(ID);

        Assert.assertTrue(materialService.deletePublication(ID, ID, user, false));

        Mockito.verify(courseRepository, Mockito.times(1)).findCourseById(ID);
        Mockito.verify(taskRepository, Mockito.times(1)).getTaskById(ID);
        Mockito.verify(taskRepository, Mockito.times(1)).delete(task);
    }
}
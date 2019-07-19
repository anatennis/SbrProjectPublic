package ru.sberbank.javaschool.edu.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.domain.UserTask;
import ru.sberbank.javaschool.edu.repository.TaskRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;
import ru.sberbank.javaschool.edu.repository.UserTaskRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest("TaskService")
public class TaskServiceTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private TaskService taskService;
    @MockBean
    private UserTaskRepository userTaskRepository;

    @Test
    public void getTaskById() {
        Task task = taskRepository.getTaskById(24);
        Mockito.doReturn(task)
                .when(taskService)
                .getTaskById(24);
        Mockito.doReturn(null)
                .when(taskService)
                .getTaskById(25);
        Mockito.doReturn(null)
                .when(taskService)
                .getTaskById(100);
    }

    @Test
    public void findUserTaskByUserAndTask() {
        User user = userRepository.findUserByLogin("anaR");
        Task task = taskRepository.getTaskById(21);
        UserTask userTask = userTaskRepository.findUserTaskByUserAndTask(user, task);
        Mockito.doReturn(userTask)
                .when(taskService)
                .findUserTaskByUserAndTask(user, task);
        Mockito.doReturn(null)
                .when(taskService)
                .findUserTaskByUserAndTask(userRepository.findUserByLogin("nick"), task);
    }

    @Test
    public void findUserTaskByTask() {
        Task task = taskRepository.getTaskById(18);
        List<UserTask> userTasks = userTaskRepository.findUserTaskByTask(task);
        Mockito.doReturn(userTasks)
                .when(taskService)
                .findUserTaskByTask(task);
        Mockito.doReturn(null)
                .when(taskService)
                .findUserTaskByTask(taskRepository.getTaskById(180));
    }

}
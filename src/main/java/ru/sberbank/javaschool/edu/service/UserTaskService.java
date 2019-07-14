package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.domain.UserTask;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;
import ru.sberbank.javaschool.edu.repository.UserTaskRepository;

import java.time.LocalDateTime;

@Service
public class UserTaskService {
    @Autowired
    private UserTaskRepository userTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public UserTask createUserTask(User user, Task task) {
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setCurMark(new Long(0));
        userTask.setTask(task);
        //userTask.setTaskState("UNCOMPLETE");
        userTaskRepository.save(userTask);
        return userTask;
    }

    public boolean submitTask(long idTask, User user, long idCourse) {
        Task task = taskRepository.getTaskById(idTask);
        UserTask userTask = userTaskRepository.findUserTaskByUserAndTask(user, task);
        if (userTask == null) {
            return false;
        }
        userTask.setTaskState("COMPLETE");
        userTask.setSubmittedDate(LocalDateTime.now());

        //send mail
        userTaskRepository.save(userTask);
        return true;

    }


    public boolean setMarkToUser(long idTask, long idCourse, long idUser, long curMark) {
        Task task = taskRepository.getTaskById(idTask);
        User user = userRepository.findUserById(idUser);
        UserTask userTask = userTaskRepository.findUserTaskByUserAndTask(user, task);
        if (userTask == null) {
            return false;
        }
        userTask.setCurMark(curMark);
        userTaskRepository.save(userTask);
        return true;
    }
}

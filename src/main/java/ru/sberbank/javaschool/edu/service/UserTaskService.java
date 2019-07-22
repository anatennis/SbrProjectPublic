package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final MailSender mailSender;

    @Autowired
    public UserTaskService(UserTaskRepository userTaskRepository, TaskRepository taskRepository,
                           UserRepository userRepository, CourseRepository courseRepository,
                           CourseUserRepository courseUserRepository, MailSender mailSender) {
        this.userTaskRepository = userTaskRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseUserRepository = courseUserRepository;
        this.mailSender = mailSender;
    }


    public UserTask createUserTask(User user, Task task) {
        Set<CourseUser> teachers = getTeachers(task.getCourse());
        for (CourseUser teacher : teachers) {
            if (teacher.getUser().getName().equals(user.getName())) {
                return null;
            }
        }
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setCurMark(new Long(0));
        userTask.setTask(task);
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
        userTaskRepository.save(userTask);

        //send mail

        Course course = courseRepository.findCourseById(idCourse);
        Set<CourseUser> teachers = getTeachers(course);

        String messageTitle = userTask.getUser().getName() + " submit task " + userTask.getTask().getTitle();
        String link = "http://localhost:8080/course/" + idCourse + "/tasks/"
                + idTask + "/" + task.getTitle() + "/" + user.getId();
        String message = String.format(
                "Здравствуйте!\n Ученик %s сдал задание %s .\n" +
                        "Посмотреть на то, как он пытался, можно здесь: " + link,
                user.getName(),
                task.getTitle()
        );
        for (CourseUser teacher : teachers) {
            if (teacher.getUser().getEmail() != null) {
                try {
                    mailSender.send(teacher.getUser().getEmail(), messageTitle, message);
                } catch (MailSendException ex) {

                }

            }
        }


        return true;

    }


    public boolean setMarkToUser(long idTask, long idUser, long curMark) {
        Task task = taskRepository.getTaskById(idTask);
        User user = userRepository.findUserById(idUser);
        UserTask userTask = userTaskRepository.findUserTaskByUserAndTask(user, task);
        if (userTask == null || userTask.getTaskState() == null ||
                curMark > task.getMaxMark() || curMark < 0) {
            return false;
        }
        userTask.setCurMark(curMark);
        userTaskRepository.save(userTask);
        return true;
    }

    public void createUserTasksForAllStudents(Task task, Course course) {
        List<CourseUser> courseUsers = courseUserRepository.findCourseUserByCourse(course);
        for (CourseUser courseUser : courseUsers) {
            createUserTask(courseUser.getUser(), task);
        }
    }

    private Set<CourseUser> getTeachers(Course course) {
        return course.getCourseUsers()
                .stream()
                .filter(u -> u.getRole() == Role.TEACHER)
                .collect(Collectors.toSet());
    }


    public void createUserTasksForNewStudent(String userLogin, Long idCourse) {
        User user = userRepository.findUserByLogin(userLogin);
        Course course = courseRepository.findCourseById(idCourse);
        List<Task> tasks = taskRepository.getTaskByCourse(course);
        for (Task task : tasks) {
            createUserTask(user, task);
        }
    }
}

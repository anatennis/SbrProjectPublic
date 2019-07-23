package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;
    private final PublicationFileRepository publicationFileRepository;

    @Autowired
    public TaskService(CourseRepository courseRepository, TaskRepository taskRepository,
                       UserTaskRepository userTaskRepository, UserRepository userRepository,
                       PublicationFileRepository publicationFileRepository) {
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
        this.userRepository = userRepository;
        this.publicationFileRepository = publicationFileRepository;
    }

    @Transactional
    public boolean createTask(Course course, User user, Task task) {
        Task taskFromDb =
                taskRepository.getTaskByCourseAndAuthorAndTitle(course, user, task.getTitle());

        if (taskFromDb == null && canCreateTask(course.getId(), user)) {
            task.setAuthor(user);
            task.setCourse(course);
            task.setCreateDate(LocalDateTime.now());;

            taskRepository.save(task);
            return true;
        }

        return false;
    }

    @Transactional
    public void editTask(Long id, Task task, User user) {
        Task taskFromDB = taskRepository.getTaskById(id);

        if (taskFromDB != null && canCreateTask(taskFromDB.getCourse().getId(), user)) {
            taskRepository.updateTask(id, task.getTitle(), task.getText(), task.getMaxMark(), task.getCompleteTime());
        }
    }

    @Transactional
    public void deleteTask(long idTask, long idCourse, User user) {

        if (canCreateTask(idCourse, user)) {
            taskRepository.deleteById(idTask);
        }
    }

    public Course findCourseById(long idCourse) {
        return courseRepository.findCourseById(idCourse);
    }

    public Task getTaskById(long idTask) {
        return taskRepository.getTaskById(idTask);
    }

    public UserTask findUserTaskByUserAndTask(User user, Task task) {
        return userTaskRepository.findUserTaskByUserAndTask(user, task);
    }

    public List<PublicationFile> findAllPubFilesByUserAndTask(User user, Task task) {
        return publicationFileRepository.findAllByUserAndPublication(user, task);
    }

    public List<UserTask> findUserTaskByTask(Task task) {
        return userTaskRepository.findUserTaskByTask(task);
    }

    public User findUserById(long idUser) {
        return userRepository.findUserById(idUser);
    }

    public List<Task> getTaskByCourseOrderById(Course course) {
        return taskRepository.getTaskByCourseOrderById(course);
    }

    public boolean canCreateTask(long idCourse, User user) {
        Course course = courseRepository.findCourseById(idCourse);

        Map<User, Role> userRoles = course.getCourseUsers()
                .stream()
                .collect(Collectors.toMap(CourseUser::getUser, CourseUser::getRole));

        return userRoles.containsKey(user) && userRoles.get(user) == Role.TEACHER;
    }
}

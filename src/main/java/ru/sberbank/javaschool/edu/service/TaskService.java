package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.*;

import java.util.List;

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
        return publicationFileRepository.findAllByUserAndTask(user, task);
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
}

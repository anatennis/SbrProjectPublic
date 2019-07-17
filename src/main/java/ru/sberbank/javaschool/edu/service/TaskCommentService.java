package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.TaskCommentRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;
import ru.sberbank.javaschool.edu.repository.UserTaskRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskCommentService {
    private final TaskCommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;

    public TaskCommentService(
            TaskCommentRepository commentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            UserTaskRepository userTaskRepository
    ) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
    }

    public boolean addComment(long idTask, long idUser, User user, TaskComment taskComment) {

        Task commonTask = taskRepository.getTaskById(idTask);
        User taskUser = userRepository.findUserById(idUser);
        UserTask task = userTaskRepository.findUserTaskByUserAndTask(taskUser, commonTask);


        if (!canAddComment(task, user)) {
            return false;
        }

        taskComment.setUserTask(task);
        taskComment.setAuthor(user);
        taskComment.setCreateDate(LocalDateTime.now());

        commentRepository.save(taskComment);

        return true;
    }

    public boolean deleteComment(User user, long id) {
        TaskComment comment = commentRepository.findTaskCommentById(id);

        if (comment == null || !cantEditComment(user, comment)) {
            return false;
        }

        commentRepository.delete(comment);

        return true;
    }

    public boolean editComment(long id, TaskComment comment, User user) {
        TaskComment commentFromDb = commentRepository.findTaskCommentById(id);

        if (commentFromDb == null || !cantEditComment(user, commentFromDb)) {
            return false;
        }

        commentFromDb.setText(comment.getText());
        commentRepository.save(commentFromDb);

        return true;
    }

    public boolean cantEditComment(User user, TaskComment comment) {

        return comment.getAuthor().equals(user);
    }

    public boolean canAddComment(UserTask task, User user) {
        Course course = task.getTask().getCourse();

        Map<User, Role> userRoles = course.getCourseUsers()
                .stream()
                .collect(Collectors.toMap(CourseUser::getUser, CourseUser::getRole));


        return task.getUser().equals(user) || userRoles.get(user) == Role.TEACHER;
    }

    public TaskComment getTaskComment(long idComment) {

        return commentRepository.findTaskCommentById(idComment);
    }
}

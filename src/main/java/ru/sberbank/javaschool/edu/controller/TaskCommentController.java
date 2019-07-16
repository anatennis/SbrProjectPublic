package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sberbank.javaschool.edu.domain.TaskComment;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.TaskCommentRepository;
import ru.sberbank.javaschool.edu.repository.UserTaskRepository;
import ru.sberbank.javaschool.edu.service.TaskCommentService;

@Controller
public class TaskCommentController {
    private final TaskCommentRepository commentRepository;
    private final TaskCommentService commentService;
    private final UserTaskRepository userTaskRepository;

    @Autowired
    public TaskCommentController(TaskCommentRepository commentRepository, TaskCommentService commentService, UserTaskRepository userTaskRepository) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.userTaskRepository = userTaskRepository;
    }

    @PostMapping("/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}/comment")
    public String addCommentTeacher(
            @PathVariable long idTask,
            @PathVariable long idUser,
            @AuthenticationPrincipal User user,
            TaskComment taskComment
    ) {

        commentService.addComment(idTask, idUser, user, taskComment);

        return "redirect:/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}";
    }

    @PostMapping("/course/{idCourse}/tasks/{idTask}/comment")
    public String addCommentStudent(
            @PathVariable long idTask,
            @AuthenticationPrincipal User user,
            TaskComment taskComment
    ) {

        commentService.addComment(idTask, user.getId(), user, taskComment);

        return "redirect:/course/{idCourse}/tasks/{idTask}";
    }
}

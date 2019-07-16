package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sberbank.javaschool.edu.domain.TaskComment;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.domain.UserTask;
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

    @GetMapping("/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}/edit_comment/{idComment}")
    public String showEditCommentTeacher(
            @PathVariable long idCourse,
            @PathVariable long idTask,
            @PathVariable String taskCaption,
            @PathVariable long idUser,
            @PathVariable long idComment,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        TaskComment comment = commentRepository.findTaskCommentById(idComment);

        if (!commentService.cantEditComment(user, comment)) {
            return "redirect:/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}";
        }

        model.addAttribute("comment", comment);
        model.addAttribute("idCourse", idCourse);
        model.addAttribute("idTask", idTask);
        model.addAttribute("taskCaption", taskCaption);
        model.addAttribute("idUser", idUser);

        return "task_comment_edit_teacher";
    }

    @GetMapping("/course/{idCourse}/tasks/{idTask}/edit_comment/{idComment}")
    public String showEditCommentStudent(
            @PathVariable long idCourse,
            @PathVariable long idTask,
            @PathVariable long idComment,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        TaskComment comment = commentRepository.findTaskCommentById(idComment);

        if (!commentService.cantEditComment(user, comment)) {
            return "redirect:/course/{idCourse}/tasks/{idTask}";
        }

        model.addAttribute("comment", comment);
        model.addAttribute("idCourse", idCourse);
        model.addAttribute("idTask", idTask);


        return "task_comment_edit_student";
    }

    @PostMapping("/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}/edit_comment/{idComment}")
    public String editCommentTeacher(
            @PathVariable long idComment,
            @AuthenticationPrincipal User user,
            TaskComment taskComment
    ) {
        commentService.editComment(idComment, taskComment, user);

        return "redirect:/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}";

    }

    @PostMapping("/course/{idCourse}/tasks/{idTask}/edit_comment/{idComment}")
    public String editCommentUser(
            @PathVariable long idComment,
            @AuthenticationPrincipal User user,
            TaskComment taskComment
    ) {
        commentService.editComment(idComment, taskComment, user);

        return "redirect:/course/{idCourse}/tasks/{idTask}";

    }

    @DeleteMapping("/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}/comment_delete/{idComment}")
    public String deleteCommentTeacher(@PathVariable long idComment, @AuthenticationPrincipal User user) {

        commentService.deleteComment(user, idComment);

        return "redirect:/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}";
    }

    @DeleteMapping("/course/{idCourse}/tasks/{idTask}/comment_delete/{idComment}")
    public String deleteCommentStudent(@PathVariable long idComment, @AuthenticationPrincipal User user) {

        commentService.deleteComment(user, idComment);

        return "redirect:/course/{idCourse}/tasks/{idTask}";
    }
}
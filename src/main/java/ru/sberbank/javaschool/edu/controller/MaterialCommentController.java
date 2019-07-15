package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.MaterialComment;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.MaterialCommentRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.service.MaterialCommentService;

@Controller
public class MaterialCommentController {
    private final MaterialRepository materialRepository;
    private final MaterialCommentService commentService;
    private final MaterialCommentRepository materialCommentRepository;

    @Autowired
    public MaterialCommentController(
            MaterialRepository materialRepository,
            MaterialCommentService commentService,
            MaterialCommentRepository materialCommentRepository
    ) {
        this.materialRepository = materialRepository;
        this.commentService = commentService;
        this.materialCommentRepository = materialCommentRepository;
    }

    @PostMapping("/course/{idCourse}/comment/{idMaterial}")
    public String addComment(@PathVariable long idMaterial,
                              @AuthenticationPrincipal User user,
                              MaterialComment comment) {
        Material material = materialRepository.getMaterialById(idMaterial);

        commentService.addComment(material, user, comment);

        return "redirect:/course/{idCourse}";
    }

    @DeleteMapping("/course/{idCourse}/delete_comment/{idComment}")
    public String deleteComment(
            @PathVariable Long idComment,
            @AuthenticationPrincipal User user
    ) {
        boolean result = commentService.deleteComment(user, idComment);
        System.out.println("------>" + result);

        return "redirect:/course/{idCourse}";

    }

    @GetMapping("/course/{idCourse}/edit_comment/{idComment}")
    public String showEditingComment(
            @PathVariable long idCourse,
            @PathVariable long idComment,
            @AuthenticationPrincipal User user,
            Model model
    ) {

        MaterialComment comment = materialCommentRepository.findMaterialCommentById(idComment);

        if (!commentService.canEditComment(user, comment)) {
            return "redirect:/course/{idCourse}";
        }

        model.addAttribute("comment", comment);
        model.addAttribute("idCourse", idCourse);

        return "material_comment_edit";
    }

    @PostMapping("/course/{idCourse}/edit_comment/{idComment}")
    public String editMaterial(
            @PathVariable Long idComment,
            MaterialComment comment,
            @AuthenticationPrincipal User user
    ) {
        commentService.editComment(idComment, comment, user);

        return "redirect:/course/{idCourse}";
    }
}

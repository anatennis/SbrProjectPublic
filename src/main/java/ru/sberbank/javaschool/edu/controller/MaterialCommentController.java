package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.MaterialComment;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.service.MaterialCommentService;

@Controller
public class MaterialCommentController {
    private final MaterialRepository materialRepository;
    private final MaterialCommentService commentService;

    @Autowired
    public MaterialCommentController(MaterialRepository materialRepository, MaterialCommentService commentService) {
        this.materialRepository = materialRepository;
        this.commentService = commentService;
    }

    @PostMapping("/course/{idCourse}/comment/{idMaterial}")
    public String addComment(@PathVariable long idMaterial,
                              @AuthenticationPrincipal User user,
                              MaterialComment comment) {
        Material material = materialRepository.getMaterialById(idMaterial);

        commentService.addComment(material, user, comment);

        return "redirect:/course/{idCourse}";
    }
}

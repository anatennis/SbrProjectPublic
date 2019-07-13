package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.MaterialComment;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.MaterialCommentRepository;

import java.time.LocalDateTime;

@Service
public class MaterialCommentService {

    private final MaterialCommentRepository commentRepository;

    @Autowired
    public MaterialCommentService(MaterialCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(Material material, User user, MaterialComment materialComment) {
        materialComment.setAuthor(user);
        materialComment.setMaterial(material);
        materialComment.setCreatedate(LocalDateTime.now());

        commentRepository.save(materialComment);
    }
}

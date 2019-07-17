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
    private final MaterialService materialService;

    @Autowired
    public MaterialCommentService(MaterialCommentRepository commentRepository, MaterialService materialService) {
        this.commentRepository = commentRepository;
        this.materialService = materialService;
    }

    public void addComment(long idMaterial, User user, MaterialComment materialComment) {
        materialComment.setAuthor(user);
        materialComment.setMaterial(materialService.getMaterialById(idMaterial));
        materialComment.setCreatedate(LocalDateTime.now());

        commentRepository.save(materialComment);
    }

    public boolean canEditComment(User user, MaterialComment comment) {

        return comment.getAuthor().equals(user);
    }

    public boolean deleteComment(User user, long id) {
        MaterialComment comment = commentRepository.findMaterialCommentById(id);

        if (comment == null || !canEditComment(user, comment)) {
            return false;
        }

        commentRepository.delete(comment);

        return true;
    }

    public boolean editComment(long id, MaterialComment comment, User user) {
        MaterialComment commentFromDb = commentRepository.findMaterialCommentById(id);

        if (commentFromDb == null || !canEditComment(user, commentFromDb)) {
            return false;
        }

        commentFromDb.setText(comment.getText());
        commentRepository.save(commentFromDb);

        return true;
    }

    public MaterialComment getMaterialComment(long idComment) {

        return commentRepository.findMaterialCommentById(idComment);
    }
}

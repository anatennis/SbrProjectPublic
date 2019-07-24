package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void addNestedComment(long idMaterial, long idParent, User user, MaterialComment childComment) {
        childComment.setAuthor(user);
        childComment.setParentComment(commentRepository.findMaterialCommentById(idParent));
        childComment.setMaterial(materialService.getMaterialById(idMaterial));
        childComment.setCreatedate(LocalDateTime.now());

        commentRepository.save(childComment);
    }

    public boolean canEditComment(User user, long id) {
        MaterialComment comment = commentRepository.findMaterialCommentById(id);

        if (comment == null) {
            return false;
        }

        return comment.getAuthor().equals(user);
    }

    @Transactional
    public void deleteComment(User user, long id) {

        if (canEditComment(user, id)) {
            commentRepository.deleteById(id);
        }
    }

    @Transactional
    public void editComment(long id, MaterialComment comment, User user) {

        if (canEditComment(user, id)) {
            commentRepository.updateMaterialComment(id, comment.getText());
        }
    }

    public MaterialComment getMaterialComment(long idComment) {

        return commentRepository.findMaterialCommentById(idComment);
    }
}

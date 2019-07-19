package ru.sberbank.javaschool.edu.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.MaterialComment;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.MaterialCommentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaterialCommentServiceTest {
    @Autowired
    private MaterialCommentService commentService;

    @MockBean
    private MaterialCommentRepository commentRepository;

    @MockBean
    private MaterialService materialService;

    private final long ID = 1L;

    @Test
    public void addComment() {
        MaterialComment comment = new MaterialComment();
        User user = new User();
        Material material = new Material();

        material.setId(ID);
        user.setId(ID);

        Mockito.doReturn(material)
                .when(materialService)
                .getMaterialById(ID);

        commentService.addComment(ID, user, comment);

        Assert.assertEquals(user, comment.getAuthor());
        Assert.assertEquals(material, comment.getMaterial());
        Assert.assertNotNull(comment.getCreatedate());

        Mockito.verify(materialService, Mockito.times(1)).getMaterialById(ID);
        Mockito.verify(commentRepository, Mockito.times(1)).save(comment);
    }

    @Test
    public void deleteComment() {
        MaterialComment comment = new MaterialComment();
        User user = new User();

        user.setId(ID);
        comment.setId(ID);
        comment.setAuthor(user);

        Mockito.doReturn(comment)
                .when(commentRepository)
                .findMaterialCommentById(ID);

        boolean isDeleted = commentService.deleteComment(user, ID);

        Assert.assertTrue(isDeleted);

        Mockito.verify(commentRepository, Mockito.times(1)).findMaterialCommentById(ID);
        Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);

    }

    @Test
    public void deleteSomeoneElseComment() {
        MaterialComment comment = new MaterialComment();
        User user = new User();
        User elseUser = new User();

        user.setId(ID);
        elseUser.setId(2L);
        comment.setId(ID);
        comment.setAuthor(elseUser);

        Mockito.doReturn(comment)
                .when(commentRepository)
                .findMaterialCommentById(ID);

        boolean isDeleted = commentService.deleteComment(user, ID);

        Assert.assertFalse(isDeleted);

        Mockito.verify(commentRepository, Mockito.times(1)).findMaterialCommentById(ID);
        Mockito.verify(commentRepository, Mockito.times(0)).delete(comment);
    }

    @Test
    public void deleteNonexistentComment() {
        MaterialComment comment = new MaterialComment();
        User user = new User();

        user.setId(ID);
        comment.setId(ID);
        comment.setAuthor(user);

        Mockito.doReturn(null)
                .when(commentRepository)
                .findMaterialCommentById(ID);

        boolean isDeleted = commentService.deleteComment(user, ID);

        Assert.assertFalse(isDeleted);

        Mockito.verify(commentRepository, Mockito.times(1)).findMaterialCommentById(ID);
        Mockito.verify(commentRepository, Mockito.times(0)).delete(comment);

    }

    @Test
    public void editComment() {
        MaterialComment comment = new MaterialComment();
        MaterialComment newComment = new MaterialComment();
        User user = new User();


        user.setId(ID);
        comment.setId(ID);
        comment.setAuthor(user);
        comment.setText("oldText");
        newComment.setText("newText");

        Mockito.doReturn(comment)
                .when(commentRepository)
                .findMaterialCommentById(ID);

        boolean isEdited = commentService.editComment(ID, newComment, user);

        Assert.assertTrue(isEdited);
        Assert.assertEquals("newText", comment.getText());

        Mockito.verify(commentRepository, Mockito.times(1)).findMaterialCommentById(ID);
        Mockito.verify(commentRepository, Mockito.times(1)).save(comment);
    }

    @Test
    public void editSomeoneElseComment() {
        MaterialComment comment = new MaterialComment();
        MaterialComment newComment = new MaterialComment();
        User user = new User();
        User elseUser = new User();

        user.setId(ID);
        elseUser.setId(2L);
        comment.setId(ID);
        comment.setAuthor(elseUser);
        comment.setText("oldText");
        newComment.setText("newText");

        Mockito.doReturn(comment)
                .when(commentRepository)
                .findMaterialCommentById(ID);

        boolean isEdited = commentService.editComment(ID, newComment, user);

        Assert.assertFalse(isEdited);

        Mockito.verify(commentRepository, Mockito.times(1)).findMaterialCommentById(ID);
        Mockito.verify(commentRepository, Mockito.times(0)).save(comment);
    }

    @Test
    public void editNonexistentComment() {
        MaterialComment comment = new MaterialComment();
        MaterialComment newComment = new MaterialComment();
        User user = new User();

        user.setId(ID);
        comment.setId(ID);
        comment.setAuthor(user);
        comment.setText("oldText");
        newComment.setText("newText");

        Mockito.doReturn(null)
                .when(commentRepository)
                .findMaterialCommentById(ID);

        boolean isEdited = commentService.editComment(ID, newComment, user);

        Assert.assertFalse(isEdited);
        Assert.assertNotEquals("newText", comment.getText());

        Mockito.verify(commentRepository, Mockito.times(1)).findMaterialCommentById(ID);
        Mockito.verify(commentRepository, Mockito.times(0)).save(comment);
    }
}
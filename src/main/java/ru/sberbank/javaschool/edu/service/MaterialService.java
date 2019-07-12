package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public boolean createMaterial(Course course, User user, Material material) {
        Material materialFromDb =
                materialRepository.getMaterialByCourseAndAuthorAndTitle(course, user, material.getTitle());

        if (materialFromDb != null) {
            return false;
        }

        material.setAuthor(user);
        material.setCourse(course);
        material.setCreateDate(LocalDateTime.now());

        materialRepository.save(material);

        return true;
    }

    public boolean removeMaterial(Long id) {
        Material material = materialRepository.getMaterialById(id);

        if (material == null) {
            return false;
        }

        materialRepository.delete(material);

        return true;
    }

    public boolean editMaterial(Long id, Material material) {
        Material materialfromDb = materialRepository.getMaterialById(id);

        if (materialfromDb == null) {
            return false;
        }

        materialfromDb.setTitle(material.getTitle());
        materialfromDb.setText(material.getText());

        materialRepository.save(materialfromDb);
        return true;
    }

    public boolean canCreateMaterial(Course course, User user) {
        Map<User, Role> userRoles = course.getCourseUsers()
                .stream()
                .collect(Collectors.toMap(CourseUser::getUser, CourseUser::getRole));

        return userRoles.containsKey(user) && userRoles.get(user) == Role.TEACHER;

    }

    public boolean canEditMaterial(Course course, Material material, User user) {
        Map<User, Role> userRoles = course.getCourseUsers()
                .stream()
                .collect(Collectors.toMap(CourseUser::getUser, CourseUser::getRole));

        if (userRoles.containsKey(user) && userRoles.get(user) == Role.TEACHER) {
            return material.getAuthor().equals(user);
        }

        return false;
    }
}

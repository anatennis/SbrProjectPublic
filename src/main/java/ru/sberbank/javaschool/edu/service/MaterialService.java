package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TaskRepository taskRepository;

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

//    public boolean canEditMaterial(Course course, Material material, User user) {
//        Map<User, Role> userRoles = course.getCourseUsers()
//                .stream()
//                .collect(Collectors.toMap(CourseUser::getUser, CourseUser::getRole));
//
//        if (userRoles.containsKey(user) && userRoles.get(user) == Role.TEACHER) {
//            return material.getAuthor().equals(user);
//        }
//
//        return false;
//    }

    //-----------------------------------------------------------------------------------------------------tasks methods

    private boolean removeTask(long idTask) {
        Task task = taskRepository.getTaskById(idTask);
        if (task == null) {
            return false;
        }

        taskRepository.delete(task);

        return true;
    }

    public boolean createTask(Course course, User user, Task task) {
        Task taskFromDb =
                taskRepository.getTaskByCourseAndAuthorAndTitle(course, user, task.getTitle());

        if (taskFromDb != null) {
            return false;
        }

        task.setAuthor(user);
        task.setCourse(course);
        task.setCreateDate(LocalDateTime.now());;

        taskRepository.save(task);

        return true;
    }

    public boolean editTask(Long id, Task task) {
        Task taskFromDB = taskRepository.getTaskById(id);

        if (taskFromDB == null) {
            return false;
        }

        taskFromDB.setTitle(task.getTitle());
        taskFromDB.setText(task.getText());
        taskFromDB.setMaxMark(task.getMaxMark());
        taskFromDB.setCompleteTime(task.getCompleteTime());

        taskRepository.save(taskFromDB);
        return true;
    }

    //----------------------------------------------------------------------------------------------------common methods

    public boolean deletePublication(long idPublication, long idCourse, User user, boolean isMaterial) {
        Course course = courseRepository.findCourseById(idCourse);
        if (canCreateMaterial(course, user)) {
            if (isMaterial) {
                return removeMaterial(idPublication);
            } else {
                return removeTask(idPublication);
            }
        }
        return false;
    }



}

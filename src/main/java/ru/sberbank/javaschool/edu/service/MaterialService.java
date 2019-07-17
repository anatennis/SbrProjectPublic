package ru.sberbank.javaschool.edu.service;

import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public MaterialService(
            MaterialRepository materialRepository,
            CourseRepository courseRepository,
            TaskRepository taskRepository
    ) {
        this.materialRepository = materialRepository;
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
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

    public boolean deleteMaterial(Long idMaterial) {
        Material material = materialRepository.getMaterialById(idMaterial);

        if (material == null) {
            return false;
        }

        materialRepository.delete(material);

        return true;
    }

    public boolean editMaterial(Long id, Material material) {
        Material materialFromDb = materialRepository.getMaterialById(id);

        if (materialFromDb == null) {
            return false;
        }

        materialFromDb.setTitle(material.getTitle());
        materialFromDb.setText(material.getText());

        materialRepository.save(materialFromDb);
        return true;
    }

    public boolean canCreateMaterial(Course course, User user) {
        Map<User, Role> userRoles = course.getCourseUsers()
                .stream()
                .collect(Collectors.toMap(CourseUser::getUser, CourseUser::getRole));

        return userRoles.containsKey(user) && userRoles.get(user) == Role.TEACHER;

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

    public boolean deletePublication(long idPublication, long idCourse, User user, boolean isMaterial) {

        Course course = courseRepository.findCourseById(idCourse);

        if (canCreateMaterial(course, user)) {
            if (isMaterial) {
                return deleteMaterial(idPublication);
            } else {
                return removeTask(idPublication);
            }
        }

        return false;
    }

    public List<Material> getCourseMaterials(long idCourse) {

        return materialRepository.getMaterialByCourseOrderById(idCourse);
    }

    public Material getMaterialById(long idMaterial) {

        return materialRepository.getMaterialById(idMaterial);
    }

    private boolean removeTask(long idTask) {
        Task task = taskRepository.getTaskById(idTask);

        if (task == null) {
            return false;
        }

        taskRepository.delete(task);

        return true;
    }
}

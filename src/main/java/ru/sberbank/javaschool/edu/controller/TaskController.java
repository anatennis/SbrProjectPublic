package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;
import ru.sberbank.javaschool.edu.service.CourseService;
import ru.sberbank.javaschool.edu.service.CourseUserService;
import ru.sberbank.javaschool.edu.service.MaterialService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class TaskController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseUserService courseUserService;
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    MaterialService materialService;

    @GetMapping("/course/{id}/tasks")
    public String showCourse(
            @PathVariable long id,
            Model model,
            @AuthenticationPrincipal User user) {
        Course course = courseRepository.findCourseById(id);
        List<Task> tasks = taskRepository.getTaskByCourseOrderById(course);

        model.addAttribute("course", course);
        model.addAttribute("tasks", tasks);
        model.addAttribute("canEdit", materialService.canCreateMaterial(course, user));

        return "tasks";
    }

    @PostMapping("/course/{idCourse}/tasks")
    public String addMaterial(@PathVariable long idCourse,
                              @AuthenticationPrincipal User user,
                               Task task) {

        Course course = courseRepository.findCourseById(idCourse);

        materialService.createTask(course, user, task);

        return "redirect:/course/{idCourse}/tasks";
    }

    @DeleteMapping("/course/{idCourse}/tasks/delete/{idTask}")
    public String removeTask(
            @PathVariable long idTask,
            @PathVariable long idCourse,
            @AuthenticationPrincipal User user) {
        materialService.deletePublication(idTask, idCourse, user, false);

        return "redirect:/course/{idCourse}/tasks";
    }

    @GetMapping("/course/{idCourse}/tasks/edit/{idTask}")
    public String showEditMaterial(
            @PathVariable Long idCourse,
            @PathVariable Long idTask,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Course course = courseRepository.findCourseById(idCourse);
        Task task = taskRepository.getTaskById(idTask);

        if (!materialService.canCreateMaterial(course, user)) {
            return "redirect:/course/{idCourse}/tasks";
        }

        model.addAttribute("course", course);
        model.addAttribute("task", task);

        return "task_edit";
    }

    @PostMapping("/course/{idCourse}/tasks/edit/{idTask}")
    public String editTask(
            @PathVariable Long idTask,
            Task task
    ) {
        materialService.editTask(idTask, task);

        return "redirect:/course/{idCourse}/tasks";
    }

    //для передачи даты из формы, пока что-то идет не так
    @InitBinder("task")
    public void initBinder(WebDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(LocalDateTime.class, "completeTime", new CustomDateEditor(
                dateFormat, true));
    }


}

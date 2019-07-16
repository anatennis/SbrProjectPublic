package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.repository.*;
import ru.sberbank.javaschool.edu.service.CourseUserService;
import ru.sberbank.javaschool.edu.service.MaterialService;
import ru.sberbank.javaschool.edu.service.PublicationFileService;
import ru.sberbank.javaschool.edu.service.UserTaskService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class TaskController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private UserTaskRepository userTaskRepository;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseUserService courseUserService;
    @Autowired
    private PublicationFileRepository publicationFileRepository;
    @Autowired
    private PublicationFileService publicationFileService;

    @GetMapping("/course/{idCourse}/tasks/{idTask}")
    public String showTask(
            @PathVariable long idCourse,
            @PathVariable long idTask,
            Model model,
            @AuthenticationPrincipal User user) {
        Course course = courseRepository.findCourseById(idCourse);
        Task task = taskRepository.getTaskById(idTask);
        UserTask userTask = userTaskRepository.findUserTaskByUserAndTask(user, task);
        if (userTask == null) {
            userTask = userTaskService.createUserTask(user, task);
        }
        List<PublicationFile> publicationFiles = publicationFileRepository.findAllByUserAndTask(user, task);

        model.addAttribute("course", course);
        model.addAttribute("task", task);
        model.addAttribute("usertask", userTask);
        model.addAttribute("isStudent", !materialService.canCreateMaterial(course, user));
        model.addAttribute("isTeacher", materialService.canCreateMaterial(course, user));
        model.addAttribute("usertasks", userTaskRepository.findUserTaskByTask(task));
        model.addAttribute("currentUser", user);
        model.addAttribute("pubFiles", publicationFiles);

        return "task";
    }

    @PostMapping("/course/{idCourse}/tasks/{idTask}")
    public String submitTask(
            @PathVariable long idCourse,
            @PathVariable long idTask,
            @RequestParam("file[]") MultipartFile files[],
            @AuthenticationPrincipal User user) {

        for (MultipartFile file : files) {
            publicationFileService.saveFiles(file, idTask, user);
        }

        userTaskService.submitTask(idTask, user, idCourse);

        return "redirect:/course/{idCourse}/tasks/{idTask}";
    }

    @GetMapping("/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}")
    public String teacherPageTask(
            @PathVariable long idCourse,
            @PathVariable long idTask,
            @PathVariable long idUser,
            @AuthenticationPrincipal User currentUser,
            Model model) {
        Course course = courseRepository.findCourseById(idCourse);
        if (!courseUserService.isTeacher(currentUser, course)) {
            return "redirect:/course/{idCourse}/tasks/{idTask}";
        }
        Task task = taskRepository.getTaskById(idTask);
        User user = userRepository.findUserById(idUser);
        List<PublicationFile> publicationFiles = publicationFileRepository.findAllByUserAndTask(user, task);
        UserTask userTask = userTaskRepository.findUserTaskByUserAndTask(user, task);

        model.addAttribute("course", course);
        model.addAttribute("task", task);
        model.addAttribute("usertask", userTask);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("pubFiles", publicationFiles);

        return "task_teacher";
    }

    @PostMapping("/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}")
    public String setMarkToUser(
            @PathVariable long idTask,
            @PathVariable long idUser,
            UserTask userTask) {

        userTaskService.setMarkToUser(idTask, idUser, userTask.getCurMark());

        return "redirect:/course/{idCourse}/tasks/{idTask}/{taskCaption}/{idUser}";
    }



    @GetMapping("/course/{id}/tasks")
    public String showTasks(
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
    public String addTask(@PathVariable long idCourse,
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
    public String showEditTask(
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

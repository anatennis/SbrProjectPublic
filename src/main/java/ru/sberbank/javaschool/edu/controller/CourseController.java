package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sberbank.javaschool.edu.domain.*;
import ru.sberbank.javaschool.edu.service.*;

import java.security.Principal;
import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;
    private final CourseUserService courseUserService;
    private final MaterialService materialService;
    private final PublicationFileService publicationFileService;
    private final UserService userService;
    private final UserTaskService userTaskService;

    @Autowired
    public CourseController(
            CourseService courseService,
            CourseUserService courseUserService,
            MaterialService materialService,
            PublicationFileService publicationFileService,
            UserService userService, UserTaskService userTaskService) {
        this.courseService = courseService;
        this.courseUserService = courseUserService;
        this.materialService = materialService;
        this.publicationFileService = publicationFileService;
        this.userService = userService;
        this.userTaskService = userTaskService;
    }

    @GetMapping("/course/{idCourse}")
    public String showCourse(
            @PathVariable long idCourse,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Course course = courseService.findCourseById(idCourse);
        List<Material> materials = course.getMaterials();
        //materialService.getCourseMaterials(idCourse); ToDo - подумать как лучше

        model.addAttribute("course", course);
        model.addAttribute("materials", materials);
        model.addAttribute("canCreate", materialService.canCreateMaterial(course, user));
        model.addAttribute("currentUser", user);

        return "course";
    }

    @GetMapping("/courses")
    public String showAllUserCourses(Model model, Principal principal) {

        List<CourseUser> allUserCourses = courseUserService.getUserCourses(principal.getName());

        model.addAttribute("courses", allUserCourses);

        return "courses";
    }

    @PostMapping("/course/{idCourse}")
    public String addMaterial(@PathVariable long idCourse,
                              @AuthenticationPrincipal User user,
                              @RequestParam("file[]") MultipartFile []files,
                              Material material
    ) {
        Course course = courseService.findCourseById(idCourse);
        materialService.createMaterial(course, user, material);
        for (MultipartFile file : files) {
            publicationFileService.saveFile(file, material.getId(), user);
        }


        return "redirect:/course/{idCourse}";
    }

    @GetMapping("/course/{idCourse}/users")
    public String showCourseUsers(@PathVariable long idCourse,
                                  Model model,
                                  @AuthenticationPrincipal User user) {

        Course course = courseService.findCourseById(idCourse);

        if (!courseUserService.isTeacher(user, course)) {
            return "redirect:/course/{idCourse}";
        }

        List<CourseUser> courseUsers = courseUserService.getCourseUsersWithoutTeachers(course);

        model.addAttribute("course", course);
        model.addAttribute("courseUsers", courseUsers);
        model.addAttribute("roles", Role.values());
        model.addAttribute("allUsers", userService.getUsersNotPresentOnCourse(idCourse));

        return "users";
    }

    @PostMapping("/course/{idCourse}/users/add")
    public String addUser(
            @PathVariable Long idCourse,
            @RequestParam String userLogin,
            @RequestParam String userRole
    ) {

        courseUserService.addCourseUser(idCourse, userLogin, userRole);
        userTaskService.createUserTasksForNewStudent(userLogin, idCourse);
        return "redirect:/course/{idCourse}/users";
    }

    @DeleteMapping("/course/{idCourse}/users/{idCourseUser}")
    public String removeCourseUser(@PathVariable long idCourseUser) {

        courseUserService.deleteCourseUser(idCourseUser);

        return "redirect:/course/{idCourse}/users";
    }



}

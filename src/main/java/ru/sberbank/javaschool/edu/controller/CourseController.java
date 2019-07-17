package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.service.CourseService;
import ru.sberbank.javaschool.edu.service.CourseUserService;
import ru.sberbank.javaschool.edu.service.MaterialService;
import ru.sberbank.javaschool.edu.service.PublicationFileService;

import java.security.Principal;
import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;
    private final CourseUserService courseUserService;
    private final MaterialService materialService;
    private final PublicationFileService publicationFileService;

    @Autowired
    public CourseController(
            CourseService courseService,
            CourseUserService courseUserService,
            MaterialService materialService,
            PublicationFileService publicationFileService) {
        this.courseService = courseService;
        this.courseUserService = courseUserService;
        this.materialService = materialService;
        this.publicationFileService = publicationFileService;
    }

    @GetMapping("/course/{idCourse}")
    public String showCourse(
            @PathVariable long idCourse,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Course course = courseService.findCourseById(idCourse);
        List<Material> materials = materialService.getCourseMaterials(idCourse);// ToDo - подумать как испарвить на course.getMaterials();

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
            publicationFileService.saveFiles(file, material.getId(), user);
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

        return "users";
    }

    @DeleteMapping("/course/{idCourse}/users/{idCourseUser}")
    public String removeCourseUser(@PathVariable long idCourseUser) {

        courseUserService.deleteCourseUser(idCourseUser);

        return "redirect:/course/{idCourse}/users";
    }



}

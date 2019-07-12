package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.service.CourseService;
import ru.sberbank.javaschool.edu.service.CourseUserService;
import ru.sberbank.javaschool.edu.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/administration")
public class AdminController {
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final CourseUserService courseUserService;

    @Autowired
    public AdminController(
            CourseRepository courseRepository,
            CourseService courseService,
            CourseUserService courseUserService
    ) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.courseUserService = courseUserService;
    }

    @GetMapping
    public String administration(Model model, Principal principal) {
        if (!hasAccess(principal)) {
            return "redirect:/";
        }
        model.addAttribute("courses", courseRepository.findAll());
        return "courseList";
    }

    @PostMapping
    public String addCourse(Course course, Model model) {
        if (!courseService.addCourse(course)) {
            model.addAttribute("message", course.getCaption() + " already exist!");
            model.addAttribute("courses", courseRepository.findAll());
            return "courseList";
        }

        return "redirect:/administration";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.removeCourse(id);
        return "redirect:/administration";
    }

    @GetMapping("/course/{id}")
    public String courseEdit(@PathVariable Long id, Model model, Principal principal) {
        if (!hasAccess(principal)) {
            return "redirect:/";
        }
        Course course = courseRepository.findCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("courseusers", course.getCourseUsers());
        return "courseEdit";
    }

    @PostMapping("/course/{id}")
    public String change(@PathVariable Long id, Course course) {
        courseService.updateCourse(id, course.getCaption());

        return "redirect:/administration/course/{id}";
    }

    @PostMapping("/course/{id}/add")
    public String addUser(
            @PathVariable Long id,
            @RequestParam String userName,
            @RequestParam String userRole
    ) {
        courseUserService.addCourseUser(id, userName, userRole);

        return "redirect:/administration/course/{id}";
    }

    private boolean hasAccess(Principal principal) {  //костыльненькое ограничение прав доступа на админку
        if (principal.getName().equals("admin")) {
            return true;
        }
        return false;
    }
}

package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.service.CourseService;


@Controller
@RequestMapping("/administration")
public class AdminController {
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Autowired
    public AdminController(CourseRepository courseRepository, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    @GetMapping
    public String administration(Model model) {
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
    public String courseEdit(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseRepository.findCourseById(id));
        return "courseEdit";
    }

    @PostMapping("/course/{id}")
    public String change(@PathVariable Long id, Course course) {
        courseService.updateCourse(id, course.getCaption());

        return "redirect:/administration/course/{id}";
    }
}

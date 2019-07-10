package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}

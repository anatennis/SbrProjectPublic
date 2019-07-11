package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.service.CourseService;
import ru.sberbank.javaschool.edu.service.CourseUserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseUserService courseUserService;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/course/{id}")
    public String showCourse(@PathVariable long id, Model model) {
        Course course = courseRepository.findCourseById(id);
        model.addAttribute("course", course);
        return "/course";
    }

    @GetMapping("/courses")
    public String showAllUserCourses(Model model, Principal principal) {
        List<CourseUser> allUserCourses = courseUserService.getUserCourses(principal.getName());
        model.addAttribute("courses", allUserCourses);
        return "/courses";
    }

}

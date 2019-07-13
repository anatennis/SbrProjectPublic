package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.service.CourseService;
import ru.sberbank.javaschool.edu.service.CourseUserService;
import ru.sberbank.javaschool.edu.service.MaterialService;

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
    MaterialRepository materialRepository;
    @Autowired
    MaterialService materialService;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/course/{id}")
    public String showCourse(
            @PathVariable long id,
            Model model,
            @AuthenticationPrincipal User user) {
        Course course = courseRepository.findCourseById(id);
        List<Material> materials = course.getMaterials();

        model.addAttribute("course", course);
        model.addAttribute("materials", materials);
        model.addAttribute("canEdit", materialService.canCreateMaterial(course, user));

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
                              Material material) {
        Course course = courseRepository.findCourseById(idCourse);
        materialService.createMaterial(course, user, material);

        return "redirect:/course/{idCourse}";
    }


}

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
import ru.sberbank.javaschool.edu.domain.Material;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.MaterialRepository;
import ru.sberbank.javaschool.edu.service.CourseService;
import ru.sberbank.javaschool.edu.service.CourseUserService;
import ru.sberbank.javaschool.edu.service.MaterialService;

@Controller
public class MaterialController {
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

    @DeleteMapping("/course/{idCourse}/delete/{idMaterial}")
    public String removeMaterial(
            @PathVariable long idMaterial,
            @PathVariable long idCourse,
            @AuthenticationPrincipal User user) {
        materialService.deletePublication(idMaterial, idCourse, user, true);

        return "redirect:/course/{idCourse}";
    }

    @GetMapping("/course/{idCourse}/edit/{idMaterial}")
    public String showEditMaterial(
            @PathVariable Long idCourse,
            @PathVariable Long idMaterial,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Course course = courseRepository.findCourseById(idCourse);
        Material material = materialRepository.getMaterialById(idMaterial);

        if (!materialService.canCreateMaterial(course, user)) {
            return "redirect:/course/{idCourse}";
        }

        model.addAttribute("course", course);
        model.addAttribute("material", material);

        return "materialEdit";
    }

    @PostMapping("/course/{idCourse}/edit/{idMaterial}")
    public String editMaterial(
            @PathVariable Long idMaterial,
            Material material
    ) {
        materialService.editMaterial(idMaterial, material);

        return "redirect:/course/{idCourse}";
    }



}

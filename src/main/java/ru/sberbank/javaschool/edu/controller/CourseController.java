package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.service.CourseService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CourseController {
    @Autowired
    CourseService courseService;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/course")
    public String showCourse(Model model, Course course) {  //работает пока не так как надо
        //model.addAttribute("caption", course.getCaption());
        return "/course";
    }

    //дубляж админского метода, но они ведут на разные страницы, в задумке это именно курсы конкретного пользователя
    @GetMapping("/courses")
    public String showAllUserCourses(Model model) {
        List<Course> allCourses = courseService.getAllCourses();
        model.addAttribute("courses", allCourses);
        return "/courses";
    }

}

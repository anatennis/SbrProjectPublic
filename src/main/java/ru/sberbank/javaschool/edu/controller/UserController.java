package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {  //TODO
    @Autowired
    private UserService userService;

    @Autowired
    HttpSession httpSession; //пока такой вариант работы с сессиями, проба

    @GetMapping
    public String userGreeting(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        String sessionInfo = userService.getInfoFromSession(httpSession, user);

        Set<CourseUser> courseUsersSet = user.getCourseUsers();
        //userService.loadUserByUsername(user.getLogin()).getUsername();
        model.addAttribute("username", username);
        model.addAttribute("oursession", sessionInfo);
        model.addAttribute("courseusers", courseUsersSet);
        return "user";
    }

    @PostMapping
    public String update(Model model, Principal principal, User user) {
        String login = principal.getName();
        if (userService.updateUser(login, user)) {
            model.addAttribute("message", "Ваши данные успешно обновлены");
        }
        return "redirect:/user";
    }

}

package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;

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
        String sessionInfo = getInfoFromSession(user);
        //userService.loadUserByUsername(user.getLogin()).getUsername();
        model.addAttribute("username", username);
        model.addAttribute("oursession", sessionInfo);
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

    private String getInfoFromSession(User user) { //положить/вытащить данные в сессию, проба
        String sessionKey = "firstAccessTime";
        String sessionKeyLogin = "userLogin";
        Object userFromSession = httpSession.getAttribute(sessionKeyLogin);
        Object time = httpSession.getAttribute(sessionKey);
        if (time == null) {
            time = LocalDateTime.now();
            httpSession.setAttribute(sessionKey, time);
        }
        if (userFromSession == null) {
            userFromSession = user.getLogin();
            httpSession.setAttribute(sessionKeyLogin, userFromSession);
        }
        return "first access time : " + time + "\nsession id: " + httpSession.getId() + "\nUser login "
                + httpSession.getAttribute(sessionKeyLogin);

    }

}

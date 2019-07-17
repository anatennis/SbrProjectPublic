package ru.sberbank.javaschool.edu.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("/")
    public String greeting(Model model) {
        return "main";
    }

    @GetMapping("/login")  //чтобы не было двойной авторизации одного юзера
    public String isLogin() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
        ) {
            return "redirect:/user";
        }
        return "/login";
    }

    @GetMapping("/test")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);

        //activateUsers();

        return "testpage";
    }


    //для активации всех аккаунтов чтобы не активировать черех почту
    //    private void activateUsers() {
    //        for (User u : userRepository.findAll()) {
    //            u.setActcode("ok");
    //        }
    //    }

}

package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.UserRepository;

@Controller
public class TestController {
    @Autowired
    private UserRepository userRepository;

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
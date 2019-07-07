package ru.sberbank.javaschool.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sberbank.javaschool.edu.repository.UserRepository;

@Controller
public class UserController {  //TODO
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/user/{user.id}")
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userlist";
    }
}

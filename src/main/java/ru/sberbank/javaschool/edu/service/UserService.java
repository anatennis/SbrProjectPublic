package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; //BCrypt так BCrypt :)

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepo.findUserByLogin(login);
    }

    public boolean addUser(User user) {
        User uFromDB = userRepo.findUserByLogin(user.getLogin());
        if (uFromDB != null) {
            return false;
        }
        user.setRegdate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);
        return true;
    }

    public boolean updateUser(String login, User user) {
        User uFromDB = userRepo.findUserByLogin(login);
        if (uFromDB == null) {
            return false;
        }
        uFromDB.setName(user.getName());
        uFromDB.setSurname(user.getSurname());
        userRepo.save(uFromDB);
        return true;
    }

    public String getInfoFromSession(HttpSession httpSession, User user) { //положить/вытащить данные в сессию, проба
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

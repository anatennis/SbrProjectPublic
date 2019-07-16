package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sberbank.javaschool.edu.domain.Course;
import ru.sberbank.javaschool.edu.domain.CourseUser;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.CourseRepository;
import ru.sberbank.javaschool.edu.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MailSender mailSender;

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

        if (user.getEmail() != null) {
            String code = UUID.randomUUID().toString();
            user.setActcode(code);
            String message = String.format(
                    "Привет, %s!\n Добро пожаловать в наш класс, мы лучше чем Гугл :D\n" +
                            "Ссылка для активации твоего аккаунта уже здесь: " +
                            "http://localhost:8080/activate/%s",
                    user.getName(),
                    code
            );
            mailSender.send(user.getEmail(), "Activation code EDUClassroom", message);
        }


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

    public List<User> getUsersNotPresentOnCourse(long idCourse) {
        List<User> users = userRepo.findAll();
        Course course = courseRepository.findCourseById(idCourse);

        List<User> presentUser = new ArrayList<>();

        for (CourseUser courseUser : course.getCourseUsers()) {
            presentUser.add(courseUser.getUser());
        }

        users.removeAll(presentUser);

        return users;
    }

    //активация юзера по почте(доработать), можно потом для отсылки
    // пароля использовать, если регистрации не будет
    public boolean isUserActivated(String code) {
        User user = userRepo.findUserByActcode(code);
        if (user == null) {
            return false;
        }
        user.setActcode("ok");
        userRepo.save(user);
        return true;
    }
}

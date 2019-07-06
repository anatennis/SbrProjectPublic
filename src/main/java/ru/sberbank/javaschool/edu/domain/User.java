package ru.sberbank.javaschool.edu.domain;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "edu_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private LocalDateTime regdate;
    private String phone;
}

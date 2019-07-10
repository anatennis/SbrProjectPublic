package ru.sberbank.javaschool.edu.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "edu_course")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "caption"})
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String caption;
    @Column(name = "createdate")
    private LocalDateTime createDate;

    @ManyToMany
    @JoinTable(name="edu_course_user",
            joinColumns = @JoinColumn(name="course", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="user", referencedColumnName="id")
    )
    private Set<User> users;

//    @ManyToMany
//    @ElementCollection
//    @CollectionTable(name = "edu_course_user", joinColumns = @JoinColumn(name = "course"))
//    @MapKeyJoinColumn(name = "user_id")
//    @Column(name = "user_role")
//    private Map<User, Role> users;



}

package ru.sberbank.javaschool.edu.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "edu_user_role")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "role"})
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String role;

    @ManyToMany
    @JoinTable(name="edu_course_user",
            joinColumns = @JoinColumn(name="role", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="user", referencedColumnName="id")
    )
    private Set<User> users;

    @ManyToMany
    @JoinTable(name="edu_course_user",
            joinColumns = @JoinColumn(name="role", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="course", referencedColumnName="id")
    )
    private Set<Course> courses;

    @Override
    public String getAuthority() {
        return getRole();
    }
}

package ru.sberbank.javaschool.edu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "edu_user_role")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "role"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String role;
}

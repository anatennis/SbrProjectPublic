package ru.sberbank.javaschool.edu.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
//@Table(name = "edu_publication")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Material")
@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
//@ToString(of = {"id", "title"})
//public class Material implements Serializable {
public class Material extends Publication {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected long id;
//    protected String title;
    @Lob
    @Column(name = "text", columnDefinition = "LONGTEXT")
    protected String text;
    @Column(name = "createdate")
    protected LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course")
    protected Course course;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    protected User author;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<MaterialComment> comments = new ArrayList<>();
}

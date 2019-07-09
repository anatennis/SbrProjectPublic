package ru.sberbank.javaschool.edu.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "edu_publication_comment")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "text", "author"})
public class MaterialComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(name = "text", columnDefinition = "LONGTEXT")
    protected String text;
    protected LocalDateTime createdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication")
    protected Material material;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    protected User author;
}

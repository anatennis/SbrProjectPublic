package ru.sberbank.javaschool.edu.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Task")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Task extends Material {
    @Column(name = "compltime")
    private LocalDateTime completeTime;
    @Column(name = "maxmark")
    private Long maxMark;
    @Column(name = "curmark")
    private Long curMark;
}

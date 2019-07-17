package ru.sberbank.javaschool.edu.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Task")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Task extends Publication {
    @Column(name = "compltime")
    private LocalDateTime completeTime;
    @Column(name = "maxmark")
    private Long maxMark;
}

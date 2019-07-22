package ru.sberbank.javaschool.edu.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Task")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Task extends Publication {
    @Column(name = "compltime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime completeTime;
    @Column(name = "maxmark")
    private Long maxMark;
}

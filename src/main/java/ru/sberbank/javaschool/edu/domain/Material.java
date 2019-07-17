package ru.sberbank.javaschool.edu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Material")
@Getter
@Setter
public class Material extends Publication {
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<MaterialComment> comments = new ArrayList<>();
}

package ru.sberbank.javaschool.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.javaschool.edu.domain.MaterialComment;

public interface MaterialCommentRepository extends JpaRepository<MaterialComment, Long> {
    MaterialComment findMaterialCommentById(Long id);
}

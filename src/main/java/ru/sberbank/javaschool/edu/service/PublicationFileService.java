package ru.sberbank.javaschool.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sberbank.javaschool.edu.domain.PublicationFile;
import ru.sberbank.javaschool.edu.domain.Task;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.PublicationFileRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PublicationFileService {
    @Autowired
    PublicationFileRepository publicationFileRepository;
    @Autowired
    TaskRepository taskRepository;
    @Value("${upload.path}")
    String uploadPath;


    public void saveFiles(MultipartFile file, long idTask, User user) {
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String filename = uuidFile + "_" + file.getOriginalFilename();
            try {
                file.transferTo(new File(uploadPath, filename));
            } catch (IOException e) {
                e.printStackTrace();
            }

            PublicationFile publicationFile = new PublicationFile();
            publicationFile.setFilename(filename);
            Task task = taskRepository.getTaskById(idTask);
            publicationFile.setTask(task);
            publicationFile.setPath(uploadPath);
            publicationFile.setUser(user);
            publicationFileRepository.save(publicationFile);
        }

    }

}

package ru.sberbank.javaschool.edu.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sberbank.javaschool.edu.domain.Publication;
import ru.sberbank.javaschool.edu.domain.PublicationFile;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.PublicationFileRepository;
import ru.sberbank.javaschool.edu.repository.PublicationRepository;
import ru.sberbank.javaschool.edu.repository.TaskRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class PublicationFileService {

    private final PublicationFileRepository publicationFileRepository;
    private final PublicationRepository publicationRepository;

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String emailPass;

    @Autowired
    public PublicationFileService(PublicationFileRepository publicationFileRepository,
                                  PublicationRepository publicationRepository) {
        this.publicationFileRepository = publicationFileRepository;
        this.publicationRepository = publicationRepository;
    }


    public void saveFile(MultipartFile file, long idPublication, User user) {
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
            //saveFileToYandex(filename);

            PublicationFile publicationFile = new PublicationFile();
            publicationFile.setFilename(filename);
            Publication publication = publicationRepository.findPublicationById(idPublication);
            publicationFile.setPublication(publication);
            publicationFile.setPath("educlassroom/");
            //publicationFile.setPath(uploadPath);
            publicationFile.setUser(user);
            publicationFileRepository.save(publicationFile);
        }

    }

    private void saveFileToYandex(String filename) {
        String URL = "https://webdav.yandex.ru/";

        Sardine sardine = SardineFactory.begin(email, emailPass);

        InputStream inStr = null;

        try {
            inStr = new FileInputStream(uploadPath+"/"+filename);
            sardine.put(URL + "educlassroom/" + filename, inStr);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        } finally {
            if (inStr != null) {
                try {
                    inStr.close();
                } catch (Exception e) {}
            }
        }

    }

    public void getFilesFromYDisk() {
        String URL = "https://webdav.yandex.ru/";

        Sardine sardine = SardineFactory.begin(email, emailPass);

        try {
            for (DavResource res : sardine.list(URL+ "educlassroom/")) {
                System.out.println(res.getHref());
//                if (!res.isDirectory()) {
//                    InputStream ins = sardine.get(URL+ "educlassroom/"+res.getDisplayName());
//
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

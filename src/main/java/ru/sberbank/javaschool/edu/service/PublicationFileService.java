package ru.sberbank.javaschool.edu.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.sardine.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sberbank.javaschool.edu.domain.Publication;
import ru.sberbank.javaschool.edu.domain.PublicationFile;
import ru.sberbank.javaschool.edu.domain.User;
import ru.sberbank.javaschool.edu.repository.PublicationFileRepository;
import ru.sberbank.javaschool.edu.repository.PublicationRepository;

import java.io.*;
import java.util.UUID;

@Service
public class PublicationFileService {

    private final PublicationFileRepository publicationFileRepository;
    private final PublicationRepository publicationRepository;

    private static final Logger logger = LoggerFactory.getLogger(PublicationFileService.class);

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
            //saveFileToYDisk(filename);

            PublicationFile publicationFile = new PublicationFile();
            publicationFile.setFilename(filename);
            Publication publication = publicationRepository.findPublicationById(idPublication);
            publicationFile.setPublication(publication);
            publicationFile.setPath("educlassroom/");
            //publicationFile.setPath(uploadPath);
            publicationFile.setUser(user);
            publicationFileRepository.save(publicationFile);

            logger.info("Successfully save pubfile to DB, filename: " + filename);
        }

    }

    private void saveFileToYDisk(String filename) {
        String URL = "https://webdav.yandex.ru/";

        Sardine sardine = SardineFactory.begin(email, emailPass);

        InputStream inStr = null;

        try {
            inStr = new FileInputStream(uploadPath + "/" + filename);
            sardine.put(URL + "educlassroom/" + filename, inStr);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        } finally {
            if (inStr != null) {
                try {
                    inStr.close();
                } catch (Exception e) {
                }
            }
        }

        logger.info("Successfully save file to YDisk, filename: " + filename);
    }

    public void getFilesFromYDisk() {
        String URL = "https://webdav.yandex.ru/";

        Sardine sardine = SardineFactory.begin(email, emailPass);

        try {
            for (DavResource res : sardine.list(URL + "educlassroom/")) {
                System.out.println(res.getHref());
                System.out.println(res.getCustomProps());
                System.out.println(res.getEtag());
                System.out.println(res.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(Long fileId) {
        PublicationFile file = publicationFileRepository.findPublicationFileById(fileId);
        if (file == null) {
            return;
        }
        try {
            deleteFileFromYDisk(file.getFilename());
            logger.info("Successfully removing file " + file.getFilename() + " from YDisk");
        } catch (IOException e) {
            logger.debug("Error during removing file " + file.getFilename() + " from YDisk");
            e.printStackTrace();
        }
        publicationFileRepository.deleteById(fileId);
        logger.info("Successfully removing file from DB");
    }

    private void deleteFileFromYDisk(String filename) throws IOException {
        String URL = "https://webdav.yandex.ru/" + "educlassroom/" + filename;
        Sardine sardine = SardineFactory.begin(email, emailPass);

        if (sardine.exists(URL)) {
            sardine.delete(URL);
        }

    }

}

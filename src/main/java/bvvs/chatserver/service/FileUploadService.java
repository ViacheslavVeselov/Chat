package bvvs.chatserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileUploadService {
    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile file) {
        String pathToFile = null;

        try {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                pathToFile = uploadPath + "/" + resultFilename;
                file.transferTo(new File(pathToFile));
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
        return Objects.requireNonNullElse(pathToFile, "File is not uploaded");
    }
}


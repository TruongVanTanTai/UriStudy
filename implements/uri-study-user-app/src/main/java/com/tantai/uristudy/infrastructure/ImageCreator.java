package com.tantai.uristudy.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageCreator {
    public String createImage(MultipartFile file, String uploadFolder) throws IOException {
        Path uploadPath = Paths.get("upload-files/" + uploadFolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + "_" + fileName;

        Path filePath = uploadPath.resolve(newFileName);
        System.out.println(filePath);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "localhost:8386/upload-files/" + uploadFolder + "/" + newFileName;
    }
}

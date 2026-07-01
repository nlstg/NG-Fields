package tg.ngstars.media.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tg.ngstars.media.config.MediaProperties;

@Service
public class FileService {

    private final MediaProperties mediaProperties;
    private final Path uploadPath;

    public FileService(MediaProperties mediaProperties) {
        this.mediaProperties = mediaProperties;
        this.uploadPath = Path.of(mediaProperties.uploadDir()).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadPath);
    }

    public String store(MultipartFile file) {
        var originalName = file.getOriginalFilename();
        var ext = "";
        if (originalName != null && originalName.contains("."))
            ext = originalName.substring(originalName.lastIndexOf('.'));
        var filename = UUID.randomUUID() + ext;

        try {
            var target = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path load(String filename) {
        var file = uploadPath.resolve(filename).normalize();
        if (!file.startsWith(uploadPath))
            throw new SecurityException("Invalid path");
        if (!Files.exists(file))
            throw new IllegalArgumentException("File not found: " + filename);
        return file;
    }

    public void delete(String filename) {
        try {
            Files.deleteIfExists(uploadPath.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}

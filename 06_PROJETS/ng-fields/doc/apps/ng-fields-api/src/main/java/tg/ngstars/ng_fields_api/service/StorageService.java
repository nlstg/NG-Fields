package tg.ngstars.ng_fields_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.UUID;

@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private static final long MAX_PHOTO_SIZE = 5 * 1024 * 1024; // 5 Mo

    private final Path uploadDir;
    private final String baseUrl;

    public StorageService(
            @Value("${storage.upload-dir:./uploads}") String uploadDir,
            @Value("${storage.base-url:http://localhost:8081}") String baseUrl) {
        this.uploadDir = Paths.get(uploadDir);
        this.baseUrl   = baseUrl;
        try {
            Files.createDirectories(this.uploadDir);
            log.info("Répertoire de stockage : {}", this.uploadDir.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le répertoire uploads", e);
        }
    }

    public String storeFile(MultipartFile file, String subfolder) throws IOException {
        if (file.getSize() > MAX_PHOTO_SIZE) {
            throw new IllegalArgumentException("Fichier trop volumineux (max 5 Mo)");
        }

        String extension = getExtension(file.getOriginalFilename());
        String filename  = UUID.randomUUID() + extension;

        Path subDir  = uploadDir.resolve(subfolder);
        Files.createDirectories(subDir);
        Files.copy(file.getInputStream(), subDir.resolve(filename),
                   StandardCopyOption.REPLACE_EXISTING);

        log.info("Fichier stocké : {}/{}", subfolder, filename);
        return baseUrl + "/api/files/" + subfolder + "/" + filename;
    }

    public String storeBase64(String base64Data, String subfolder) throws IOException {
        String data = base64Data.replaceAll("^data:image/[^;]+;base64,", "");
        byte[] bytes = Base64.getDecoder().decode(data);

        String filename = UUID.randomUUID() + ".png";
        Path subDir = uploadDir.resolve(subfolder);
        Files.createDirectories(subDir);
        Files.write(subDir.resolve(filename), bytes);

        log.info("Signature stockée : {}/{}", subfolder, filename);
        return baseUrl + "/api/files/" + subfolder + "/" + filename;
    }

    public byte[] loadFile(String subfolder, String filename) throws IOException {
        return Files.readAllBytes(uploadDir.resolve(subfolder).resolve(filename));
    }

    public byte[] loadFromUrl(String url) throws IOException {
        String path = url.replace(baseUrl + "/api/files/", "");
        String[] parts = path.split("/", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("URL invalide : " + url);
        }
        return loadFile(parts[0], parts[1]);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".bin";
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }
}

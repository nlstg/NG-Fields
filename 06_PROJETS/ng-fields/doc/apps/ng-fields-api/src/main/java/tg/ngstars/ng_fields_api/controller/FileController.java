package tg.ngstars.ng_fields_api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.ngstars.ng_fields_api.service.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageService storageService;

    @Value("${storage.upload-dir:./uploads}")
    private String uploadDir;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{subfolder}/{filename}")
    public ResponseEntity<byte[]> serveFile(
            @PathVariable String subfolder,
            @PathVariable String filename) throws IOException {

        byte[] data = storageService.loadFile(subfolder, filename);

        String contentType = Files.probeContentType(
            Paths.get(uploadDir, subfolder, filename)
        );
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(data);
    }
}

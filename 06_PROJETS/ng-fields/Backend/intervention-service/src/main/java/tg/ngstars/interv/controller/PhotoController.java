package tg.ngstars.interv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tg.ngstars.interv.dto.PhotoResponse;
import tg.ngstars.interv.service.PhotoService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interventions/{id}/photos")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<PhotoResponse> upload(
            @PathVariable UUID id,
            @RequestParam("file")      MultipartFile file,
            @RequestParam("type")      String type,
            @RequestParam(value = "latitude",  required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude
    ) throws IOException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(photoService.addPhoto(id, file, type, latitude, longitude));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PhotoResponse>> list(@PathVariable UUID id) {
        return ResponseEntity.ok(photoService.listPhotos(id));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PhotoResponse>> listByType(
            @PathVariable UUID id,
            @PathVariable String type) {
        return ResponseEntity.ok(photoService.listPhotosByType(id, type));
    }

    @DeleteMapping("/{photoId}")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @PathVariable UUID photoId) {
        photoService.deletePhoto(id, photoId);
        return ResponseEntity.noContent().build();
    }
}

package tg.ngstars.interv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tg.ngstars.interv.client.MediaClient;
import tg.ngstars.interv.dto.PhotoResponse;
import tg.ngstars.interv.model.InterventionPhoto;
import tg.ngstars.interv.model.PhotoType;
import tg.ngstars.interv.repository.InterventionPhotoRepository;
import tg.ngstars.interv.repository.InterventionRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PhotoService {

    private static final Logger log = LoggerFactory.getLogger(PhotoService.class);
    private static final int MAX_PHOTOS_PER_CATEGORY = 5;

    private final InterventionPhotoRepository photoRepo;
    private final InterventionRepository interventionRepo;
    private final MediaClient mediaClient;

    public PhotoService(
            InterventionPhotoRepository photoRepo,
            InterventionRepository interventionRepo,
            MediaClient mediaClient) {
        this.photoRepo        = photoRepo;
        this.interventionRepo = interventionRepo;
        this.mediaClient      = mediaClient;
    }

    public PhotoResponse addPhoto(
            UUID interventionId,
            MultipartFile file,
            String type,
            Double latitude,
            Double longitude) throws IOException {

        var intervention = interventionRepo.findById(interventionId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Intervention introuvable : " + interventionId));

        PhotoType photoType = PhotoType.valueOf(type.toUpperCase());

        long count = photoRepo.countByInterventionIdAndType(interventionId, photoType);
        if (count >= MAX_PHOTOS_PER_CATEGORY) {
            throw new IllegalStateException(
                "Limite atteinte : maximum " + MAX_PHOTOS_PER_CATEGORY
                + " photos par catégorie (" + type + ")");
        }

        String url = mediaClient.uploadFile(file);

        InterventionPhoto photo = new InterventionPhoto();
        photo.setIntervention(intervention);
        photo.setUrl(url);
        photo.setType(photoType);
        photo.setLatitude(latitude);
        photo.setLongitude(longitude);
        photo.setTakenAt(Instant.now());
        photo.setOriginalFilename(file.getOriginalFilename());

        InterventionPhoto saved = photoRepo.save(photo);
        log.info("Photo {} ajoutée à l'intervention {} (total {} {})",
            saved.getId(), interventionId, count + 1, type);

        return PhotoResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> listPhotos(UUID interventionId) {
        return photoRepo.findByInterventionId(interventionId)
            .stream().map(PhotoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> listPhotosByType(UUID interventionId, String type) {
        PhotoType photoType = PhotoType.valueOf(type.toUpperCase());
        return photoRepo.findByInterventionIdAndType(interventionId, photoType)
            .stream().map(PhotoResponse::from).toList();
    }

    public void deletePhoto(UUID interventionId, UUID photoId) {
        InterventionPhoto photo = photoRepo.findById(photoId)
            .filter(p -> p.getIntervention().getId().equals(interventionId))
            .orElseThrow(() -> new IllegalArgumentException("Photo introuvable : " + photoId));
        photoRepo.delete(photo);
        log.info("Photo {} supprimée de l'intervention {}", photoId, interventionId);
    }
}

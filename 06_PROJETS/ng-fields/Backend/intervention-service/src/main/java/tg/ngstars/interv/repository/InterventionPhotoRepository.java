package tg.ngstars.interv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.ngstars.interv.model.InterventionPhoto;
import tg.ngstars.interv.model.PhotoType;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterventionPhotoRepository extends JpaRepository<InterventionPhoto, UUID> {

    List<InterventionPhoto> findByInterventionId(UUID interventionId);

    List<InterventionPhoto> findByInterventionIdAndType(UUID interventionId, PhotoType type);

    long countByInterventionIdAndType(UUID interventionId, PhotoType type);
}

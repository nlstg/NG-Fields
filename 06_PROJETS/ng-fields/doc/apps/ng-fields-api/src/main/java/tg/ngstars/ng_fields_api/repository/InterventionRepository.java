package tg.ngstars.ng_fields_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.ngstars.ng_fields_api.model.Intervention;
import tg.ngstars.ng_fields_api.model.InterventionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, UUID> {

    List<Intervention> findByTechnicianIdOrderByDateDesc(String technicianId);

    List<Intervention> findByClientIdOrderByDateDesc(UUID clientId);

    List<Intervention> findByStatusOrderByDateDesc(InterventionStatus status);

    Optional<Intervention> findByLocalId(String localId);

    List<Intervention> findAllByOrderByDateDesc();

    @Query("SELECT i FROM Intervention i WHERE i.syncedAt IS NULL ORDER BY i.createdAt ASC")
    List<Intervention> findUnsynchronized();
}

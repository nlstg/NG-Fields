package tg.ngstars.interv.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tg.ngstars.interv.model.Intervention;

public interface InterventionRepository extends JpaRepository<Intervention, UUID> {

    Optional<Intervention> findByReference(String reference);

    List<Intervention> findByClientIdOrderByCreatedAtDesc(UUID clientId);

    Page<Intervention> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByReference(String reference);
}

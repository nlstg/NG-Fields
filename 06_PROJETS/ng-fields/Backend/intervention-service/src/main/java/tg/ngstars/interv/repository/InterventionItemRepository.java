package tg.ngstars.interv.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tg.ngstars.interv.model.InterventionItem;

public interface InterventionItemRepository extends JpaRepository<InterventionItem, UUID> {

    List<InterventionItem> findByInterventionIdOrderByCreatedAt(UUID interventionId);
}

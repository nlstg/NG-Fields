package tg.ngstars.ng_fields_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.ngstars.ng_fields_api.model.InterventionItem;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterventionItemRepository extends JpaRepository<InterventionItem, UUID> {

    List<InterventionItem> findByInterventionId(UUID interventionId);

    void deleteByInterventionId(UUID interventionId);
}

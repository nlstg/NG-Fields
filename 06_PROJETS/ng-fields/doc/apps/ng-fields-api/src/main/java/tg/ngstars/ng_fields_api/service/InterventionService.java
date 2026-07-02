package tg.ngstars.ng_fields_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.ngstars.ng_fields_api.dto.*;
import tg.ngstars.ng_fields_api.model.*;
import tg.ngstars.ng_fields_api.repository.ClientRepository;
import tg.ngstars.ng_fields_api.repository.InterventionItemRepository;
import tg.ngstars.ng_fields_api.repository.InterventionRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class InterventionService {

    private static final Logger log = LoggerFactory.getLogger(InterventionService.class);

    private final InterventionRepository interventionRepo;
    private final InterventionItemRepository itemRepo;
    private final ClientRepository clientRepo;
    private final AuditService auditService;

    public InterventionService(
            InterventionRepository interventionRepo,
            InterventionItemRepository itemRepo,
            ClientRepository clientRepo,
            AuditService auditService) {
        this.interventionRepo = interventionRepo;
        this.itemRepo = itemRepo;
        this.clientRepo = clientRepo;
        this.auditService = auditService;
    }

    public InterventionResponse create(CreateInterventionRequest req, Jwt jwt) {
        if (req.localId() != null) {
            var existing = interventionRepo.findByLocalId(req.localId());
            if (existing.isPresent()) {
                log.info("Intervention deja synchronisee (local_id={})", req.localId());
                return InterventionResponse.from(existing.get());
            }
        }

        Client client = clientRepo.findById(req.clientId())
            .orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + req.clientId()));

        Intervention i = new Intervention();
        i.setLocalId(req.localId());
        i.setDate(req.date());
        i.setClient(client);
        i.setType(req.type());
        i.setTechnicianId(jwt.getSubject());
        i.setTechnicianName(
            jwt.getClaimAsString("given_name") + " " + jwt.getClaimAsString("family_name")
        );

        applyUpdateFields(i, req);

        Intervention saved = interventionRepo.save(i);
        log.info("Intervention creee : {} (technicien={})", saved.getId(), saved.getTechnicianName());
        auditService.log("CREATE", "Intervention", saved.getId(), Map.of(
            "localId", saved.getLocalId(),
            "clientId", saved.getClient().getId().toString()
        ));
        return InterventionResponse.from(saved);
    }

    public InterventionResponse update(UUID id, UpdateInterventionRequest req) {
        Intervention i = findOrThrow(id);
        applyPatch(i, req);
        Intervention saved = interventionRepo.save(i);
        auditService.log("UPDATE", "Intervention", id, Map.of("status", saved.getStatus().name()));
        return InterventionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> listAll() {
        return interventionRepo.findAllByOrderByDateDesc()
            .stream().map(InterventionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> listByTechnician(String technicianId) {
        return interventionRepo.findByTechnicianIdOrderByDateDesc(technicianId)
            .stream().map(InterventionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> listByClient(UUID clientId) {
        return interventionRepo.findByClientIdOrderByDateDesc(clientId)
            .stream().map(InterventionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InterventionResponse get(UUID id) {
        return InterventionResponse.from(findOrThrow(id));
    }

    public InterventionItemResponse addItem(UUID interventionId, InterventionItemRequest req) {
        Intervention i = findOrThrow(interventionId);
        InterventionItem item = new InterventionItem();
        item.setName(req.name());
        item.setQuantity(req.quantity() != null ? req.quantity() : 1);
        item.setIntervention(i);
        InterventionItemResponse response = InterventionItemResponse.from(itemRepo.save(item));
        auditService.log("CREATE", "InterventionItem", interventionId, Map.of("itemName", req.name()));
        return response;
    }

    public InterventionItemResponse updateItem(UUID interventionId, UUID itemId,
                                               InterventionItemRequest req) {
        InterventionItem item = itemRepo.findById(itemId)
            .filter(it -> it.getIntervention().getId().equals(interventionId))
            .orElseThrow(() -> new IllegalArgumentException("Piece introuvable : " + itemId));
        item.setName(req.name());
        item.setQuantity(req.quantity());
        InterventionItemResponse response = InterventionItemResponse.from(itemRepo.save(item));
        auditService.log("UPDATE", "InterventionItem", interventionId, Map.of("itemName", req.name()));
        return response;
    }

    public void deleteItem(UUID interventionId, UUID itemId) {
        InterventionItem item = itemRepo.findById(itemId)
            .filter(it -> it.getIntervention().getId().equals(interventionId))
            .orElseThrow(() -> new IllegalArgumentException("Piece introuvable : " + itemId));
        itemRepo.delete(item);
        auditService.log("DELETE", "InterventionItem", interventionId, Map.of("itemName", item.getName()));
    }

    @Transactional(readOnly = true)
    public List<InterventionItemResponse> listItems(UUID interventionId) {
        return itemRepo.findByInterventionId(interventionId)
            .stream().map(InterventionItemResponse::from).toList();
    }

    private Intervention findOrThrow(UUID id) {
        return interventionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Intervention introuvable : " + id));
    }

    private void applyUpdateFields(Intervention i, CreateInterventionRequest req) {
        i.setDepartureTime(req.departureTime());
        i.setArrivalTime(req.arrivalTime());
        i.setInterventionStartTime(req.interventionStartTime());
        i.setInterventionEndTime(req.interventionEndTime());
        i.setReturnTime(req.returnTime());
        i.setProblemDesc(req.problemDesc());
        i.setOpenProjectTicketId(req.openProjectTicketId());
        i.setDiagnosis(req.diagnosis());
        i.setWorkDone(req.workDone());
        i.setEquipmentType(req.equipmentType());
        i.setEquipmentBrand(req.equipmentBrand());
        i.setEquipmentModel(req.equipmentModel());
        i.setEquipmentSerial(req.equipmentSerial());
        i.setEquipmentLocation(req.equipmentLocation());
        if (req.result() != null) {
            i.setResult(InterventionResult.valueOf(req.result()));
        }
        i.setRecommendations(req.recommendations());
        i.setBillable(req.billable() != null ? req.billable() : true);
        i.setBillingNotes(req.billingNotes());
    }

    private void applyPatch(Intervention i, UpdateInterventionRequest req) {
        if (req.type() != null)                   i.setType(req.type());
        if (req.departureTime() != null)          i.setDepartureTime(req.departureTime());
        if (req.arrivalTime() != null)            i.setArrivalTime(req.arrivalTime());
        if (req.interventionStartTime() != null)  i.setInterventionStartTime(req.interventionStartTime());
        if (req.interventionEndTime() != null)    i.setInterventionEndTime(req.interventionEndTime());
        if (req.returnTime() != null)             i.setReturnTime(req.returnTime());
        if (req.problemDesc() != null)            i.setProblemDesc(req.problemDesc());
        if (req.openProjectTicketId() != null)    i.setOpenProjectTicketId(req.openProjectTicketId());
        if (req.diagnosis() != null)              i.setDiagnosis(req.diagnosis());
        if (req.workDone() != null)               i.setWorkDone(req.workDone());
        if (req.equipmentType() != null)          i.setEquipmentType(req.equipmentType());
        if (req.equipmentBrand() != null)         i.setEquipmentBrand(req.equipmentBrand());
        if (req.equipmentModel() != null)         i.setEquipmentModel(req.equipmentModel());
        if (req.equipmentSerial() != null)        i.setEquipmentSerial(req.equipmentSerial());
        if (req.equipmentLocation() != null)      i.setEquipmentLocation(req.equipmentLocation());
        if (req.result() != null)                 i.setResult(InterventionResult.valueOf(req.result()));
        if (req.recommendations() != null)        i.setRecommendations(req.recommendations());
        if (req.billable() != null)               i.setBillable(req.billable());
        if (req.billingNotes() != null)           i.setBillingNotes(req.billingNotes());
    }
}

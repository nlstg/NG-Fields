package tg.ngstars.interv.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tg.ngstars.interv.dto.CreateInterventionRequest;
import tg.ngstars.interv.dto.CreateInterventionRequest.CreateItemRequest;
import tg.ngstars.interv.dto.InterventionResponse;
import tg.ngstars.interv.dto.InterventionResponse.ItemResponse;
import tg.ngstars.interv.model.Intervention;
import tg.ngstars.interv.model.InterventionItem;
import tg.ngstars.interv.repository.InterventionRepository;

@Service
@Transactional(readOnly = true)
public class InterventionService {

    private final InterventionRepository interventionRepository;

    public InterventionService(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    @Transactional
    public InterventionResponse createIntervention(CreateInterventionRequest request, UUID userId) {
        if (interventionRepository.existsByReference(request.reference()))
            throw new IllegalArgumentException("Reference already exists: " + request.reference());

        var intervention = Intervention.builder()
                .reference(request.reference())
                .clientId(request.clientId())
                .clientName(request.clientName())
                .clientEmail(request.clientEmail())
                .clientPhone(request.clientPhone())
                .clientAddress(request.clientAddress())
                .equipmentType(request.equipmentType())
                .equipmentBrand(request.equipmentBrand())
                .equipmentModel(request.equipmentModel())
                .equipmentSerial(request.equipmentSerial())
                .reportedIssue(request.reportedIssue())
                .diagnosis(request.diagnosis())
                .status(request.status() != null ? request.status() : "PENDING")
                .interventionDate(request.interventionDate())
                .createdBy(userId)
                .assignedTo(request.assignedTo())
                .siteAddress(request.siteAddress())
                .siteCity(request.siteCity())
                .estimatedCost(request.estimatedCost())
                .notes(request.notes())
                .active(true)
                .build();

        if (request.items() != null) {
            var items = request.items().stream().map(itemReq -> {
                var unitPrice = itemReq.unitPrice() != null ? itemReq.unitPrice() : BigDecimal.ZERO;
                var quantity = itemReq.quantity() != null ? itemReq.quantity() : 1;
                return InterventionItem.builder()
                        .intervention(intervention)
                        .type(itemReq.type())
                        .description(itemReq.description())
                        .quantity(quantity)
                        .unitPrice(unitPrice)
                        .total(unitPrice.multiply(BigDecimal.valueOf(quantity)))
                        .build();
            }).toList();
            intervention.setItems(items);
            intervention.setTotalCost(intervention.getItems().stream()
                    .map(InterventionItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return toResponse(interventionRepository.save(intervention));
    }

    public Page<InterventionResponse> getInterventions(Pageable pageable) {
        return interventionRepository.findByActiveTrueOrderByCreatedAtDesc(pageable)
                .map(this::toResponse);
    }

    public InterventionResponse getIntervention(UUID id) {
        return toResponse(interventionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Intervention not found: " + id)));
    }

    @Transactional
    public InterventionResponse updateIntervention(UUID id, CreateInterventionRequest request) {
        var intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Intervention not found: " + id));

        intervention.setReference(request.reference());
        intervention.setClientId(request.clientId());
        intervention.setClientName(request.clientName());
        intervention.setClientEmail(request.clientEmail());
        intervention.setClientPhone(request.clientPhone());
        intervention.setClientAddress(request.clientAddress());
        intervention.setEquipmentType(request.equipmentType());
        intervention.setEquipmentBrand(request.equipmentBrand());
        intervention.setEquipmentModel(request.equipmentModel());
        intervention.setEquipmentSerial(request.equipmentSerial());
        intervention.setReportedIssue(request.reportedIssue());
        intervention.setDiagnosis(request.diagnosis());
        if (request.status() != null) intervention.setStatus(request.status());
        intervention.setInterventionDate(request.interventionDate());
        intervention.setAssignedTo(request.assignedTo());
        intervention.setSiteAddress(request.siteAddress());
        intervention.setSiteCity(request.siteCity());
        intervention.setEstimatedCost(request.estimatedCost());
        intervention.setNotes(request.notes());

        if (request.items() != null) {
            intervention.getItems().clear();
            var items = request.items().stream().map(itemReq -> {
                var unitPrice = itemReq.unitPrice() != null ? itemReq.unitPrice() : BigDecimal.ZERO;
                var quantity = itemReq.quantity() != null ? itemReq.quantity() : 1;
                return InterventionItem.builder()
                        .intervention(intervention)
                        .type(itemReq.type())
                        .description(itemReq.description())
                        .quantity(quantity)
                        .unitPrice(unitPrice)
                        .total(unitPrice.multiply(BigDecimal.valueOf(quantity)))
                        .build();
            }).toList();
            intervention.getItems().addAll(items);
            intervention.setTotalCost(items.stream()
                    .map(InterventionItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public void deleteIntervention(UUID id) {
        var intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Intervention not found: " + id));
        intervention.setActive(false);
        interventionRepository.save(intervention);
    }

    public List<InterventionResponse> getClientInterventions(UUID clientId) {
        return interventionRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream()
                .map(this::toResponse).toList();
    }

    public byte[] generatePdf(UUID id) {
        var intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Intervention not found: " + id));
        return PdfService.generate(intervention);
    }

    private InterventionResponse toResponse(Intervention i) {
        var items = i.getItems() != null
                ? i.getItems().stream().<ItemResponse>map(item -> new ItemResponse(
                        item.getId(), item.getType(), item.getDescription(),
                        item.getQuantity(), item.getUnitPrice(), item.getTotal(),
                        item.getCreatedAt())).toList()
                : List.<ItemResponse>of();

        return new InterventionResponse(
                i.getId(), i.getReference(), i.getClientId(),
                i.getClientName(), i.getClientEmail(), i.getClientPhone(),
                i.getClientAddress(), i.getEquipmentType(), i.getEquipmentBrand(),
                i.getEquipmentModel(), i.getEquipmentSerial(), i.getReportedIssue(),
                i.getDiagnosis(), i.getStatus(), i.getInterventionDate(),
                i.getCreatedBy(), i.getAssignedTo(), i.getSiteAddress(),
                i.getSiteCity(), i.getEstimatedCost(), i.getTotalCost(),
                i.getClientSignature(), i.getTechnicianSignature(), i.getSignedAt(),
                i.getNotes(), i.getActive(), i.getCreatedAt(), i.getUpdatedAt(), items);
    }
}

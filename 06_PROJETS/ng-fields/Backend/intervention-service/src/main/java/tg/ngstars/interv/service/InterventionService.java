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
import tg.ngstars.interv.dto.ItemRequest;
import tg.ngstars.interv.dto.SyncRequest;
import tg.ngstars.interv.dto.UpdateBillingRequest;
import tg.ngstars.interv.dto.UpdateDiagnosisRequest;
import tg.ngstars.interv.dto.UpdateEquipmentRequest;
import tg.ngstars.interv.dto.UpdateRecommendationsRequest;
import tg.ngstars.interv.dto.UpdateResultRequest;
import tg.ngstars.interv.dto.UpdateScheduleRequest;
import tg.ngstars.interv.exception.ForbiddenException;
import tg.ngstars.interv.exception.NotFoundException;
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

    private Intervention findOrThrow(UUID id) {
        return interventionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Intervention not found: " + id));
    }

    void checkOwnership(Intervention intervention, UUID userId, boolean isAdminOrManager) {
        if (!isAdminOrManager && !intervention.getAssignedTo().equals(userId))
            throw new ForbiddenException("Not assigned to this intervention");
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
                .equipmentLocation(request.equipmentLocation())
                .reportedIssue(request.reportedIssue())
                .openprojectTicketId(request.openprojectTicketId())
                .openprojectTicketUrl(request.openprojectTicketUrl())
                .diagnosis(request.diagnosis())
                .workDone(request.workDone())
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

    public Page<InterventionResponse> getInterventions(String status, UUID technicianId, Pageable pageable) {
        if (technicianId != null)
            return (status != null
                    ? interventionRepository.findByActiveTrueAndAssignedToAndStatusOrderByCreatedAtDesc(technicianId, status, pageable)
                    : interventionRepository.findByActiveTrueAndAssignedToOrderByCreatedAtDesc(technicianId, pageable))
                    .map(this::toResponse);
        if (status != null)
            return interventionRepository.findByActiveTrueAndStatusOrderByCreatedAtDesc(status, pageable)
                    .map(this::toResponse);
        return interventionRepository.findByActiveTrueOrderByCreatedAtDesc(pageable)
                .map(this::toResponse);
    }

    public InterventionResponse getIntervention(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public InterventionResponse updateIntervention(UUID id, CreateInterventionRequest request) {
        var intervention = findOrThrow(id);

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
        intervention.setEquipmentLocation(request.equipmentLocation());
        intervention.setReportedIssue(request.reportedIssue());
        intervention.setOpenprojectTicketId(request.openprojectTicketId());
        intervention.setOpenprojectTicketUrl(request.openprojectTicketUrl());
        intervention.setDiagnosis(request.diagnosis());
        intervention.setWorkDone(request.workDone());
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
        var intervention = findOrThrow(id);
        intervention.setActive(false);
        interventionRepository.save(intervention);
    }

    public List<InterventionResponse> getClientInterventions(UUID clientId) {
        return interventionRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream()
                .map(this::toResponse).toList();
    }

    public byte[] generatePdf(UUID id) {
        var intervention = findOrThrow(id);
        return PdfService.generate(intervention);
    }

    @Transactional
    public InterventionResponse updateSchedule(UUID id, UpdateScheduleRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        if (request.departureTime() != null) intervention.setDepartureTime(request.departureTime());
        if (request.arrivalTime() != null) intervention.setArrivalTime(request.arrivalTime());
        if (request.startTime() != null) intervention.setStartTime(request.startTime());
        if (request.endTime() != null) {
            intervention.setEndTime(request.endTime());
            if (intervention.getStartTime() != null)
                intervention.setDurationMinutes((int) java.time.Duration.between(intervention.getStartTime(), request.endTime()).toMinutes());
        }
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse updateEquipment(UUID id, UpdateEquipmentRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        if (request.brand() != null) intervention.setEquipmentBrand(request.brand());
        if (request.model() != null) intervention.setEquipmentModel(request.model());
        if (request.serial() != null) intervention.setEquipmentSerial(request.serial());
        if (request.location() != null) intervention.setEquipmentLocation(request.location());
        if (request.problemDescription() != null) intervention.setReportedIssue(request.problemDescription());
        if (request.openprojectTicketId() != null) intervention.setOpenprojectTicketId(request.openprojectTicketId());
        if (request.openprojectTicketUrl() != null) intervention.setOpenprojectTicketUrl(request.openprojectTicketUrl());
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse updateDiagnosis(UUID id, UpdateDiagnosisRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        if (request.diagnosis() != null) intervention.setDiagnosis(request.diagnosis());
        if (request.workDone() != null) intervention.setWorkDone(request.workDone());
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse updateResult(UUID id, UpdateResultRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        intervention.setResult(request.result());
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse updateRecommendations(UUID id, UpdateRecommendationsRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        intervention.setRecommendations(request.recommendations());
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse updateBilling(UUID id, UpdateBillingRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        intervention.setBillable(request.billable());
        intervention.setBillingAmount(request.billingAmount());
        intervention.setBillingNotes(request.billingNotes());
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse addItem(UUID id, ItemRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        var unitPrice = request.unitPrice() != null ? request.unitPrice() : BigDecimal.ZERO;
        var quantity = request.quantity() != null ? request.quantity() : 1;
        var item = InterventionItem.builder()
                .intervention(intervention)
                .type(request.type())
                .description(request.description())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .total(unitPrice.multiply(BigDecimal.valueOf(quantity)))
                .build();
        intervention.getItems().add(item);
        intervention.setTotalCost(intervention.getItems().stream()
                .map(InterventionItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse updateItem(UUID interventionId, UUID itemId, ItemRequest request, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(interventionId);
        checkOwnership(intervention, userId, isAdminOrManager);
        var item = intervention.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        if (request.type() != null) item.setType(request.type());
        if (request.description() != null) item.setDescription(request.description());
        if (request.quantity() != null) item.setQuantity(request.quantity());
        if (request.unitPrice() != null) item.setUnitPrice(request.unitPrice());
        item.setTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        intervention.setTotalCost(intervention.getItems().stream()
                .map(InterventionItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse removeItem(UUID interventionId, UUID itemId, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(interventionId);
        checkOwnership(intervention, userId, isAdminOrManager);
        var removed = intervention.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) throw new NotFoundException("Item not found: " + itemId);
        intervention.setTotalCost(intervention.getItems().stream()
                .map(InterventionItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse closeIntervention(UUID id, UUID userId, boolean isAdminOrManager) {
        var intervention = findOrThrow(id);
        checkOwnership(intervention, userId, isAdminOrManager);
        if (intervention.getClientSignature() != null
                && intervention.getTechnicianSignature() != null
                && intervention.getManagerSignature() != null) {
            intervention.setStatus("COMPLETED");
            intervention.setSignedAt(java.time.OffsetDateTime.now());
        }
        return toResponse(interventionRepository.save(intervention));
    }

    @Transactional
    public InterventionResponse syncFromMobile(SyncRequest request, UUID userId) {
        var existing = interventionRepository.findByLocalId(request.localId());
        if (existing.isPresent()) {
            var intervention = existing.get();
            intervention.setStatus(request.status() != null ? request.status() : intervention.getStatus());
            intervention.setInterventionDate(request.interventionDate() != null ? request.interventionDate() : intervention.getInterventionDate());
            intervention.setClientEmail(request.clientEmail() != null ? request.clientEmail() : intervention.getClientEmail());
            intervention.setClientPhone(request.clientPhone() != null ? request.clientPhone() : intervention.getClientPhone());
            if (request.clientName() != null) intervention.setClientName(request.clientName());
            intervention.setClientEmail(request.clientEmail() != null ? request.clientEmail() : intervention.getClientEmail());
            intervention.setClientPhone(request.clientPhone() != null ? request.clientPhone() : intervention.getClientPhone());
            intervention.setClientAddress(request.clientAddress() != null ? request.clientAddress() : intervention.getClientAddress());
            return toResponse(interventionRepository.save(intervention));
        }
        var intervention = Intervention.builder()
                .reference(request.localId())
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
                .status(request.status() != null ? request.status() : "PENDING")
                .interventionDate(request.interventionDate())
                .createdBy(userId)
                .assignedTo(userId)
                .siteAddress(request.siteAddress())
                .siteCity(request.siteCity())
                .localId(request.localId())
                .active(true)
                .build();
        return toResponse(interventionRepository.save(intervention));
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
                i.getEquipmentModel(), i.getEquipmentSerial(),
                i.getEquipmentLocation(), i.getReportedIssue(),
                i.getOpenprojectTicketId(), i.getOpenprojectTicketUrl(),
                i.getDiagnosis(), i.getWorkDone(), i.getStatus(), i.getInterventionDate(),
                i.getCreatedBy(), i.getAssignedTo(), i.getSiteAddress(),
                i.getSiteCity(), i.getEstimatedCost(), i.getTotalCost(),
                i.getClientSignature(), i.getTechnicianSignature(), i.getManagerSignature(), i.getSignedAt(),
                i.getDepartureTime(), i.getArrivalTime(), i.getStartTime(), i.getEndTime(), i.getDurationMinutes(),
                i.getResult(), i.getRecommendations(), i.getBillable(), i.getBillingAmount(), i.getBillingNotes(),
                i.getLocalId(), i.getNotes(), i.getActive(), i.getCreatedAt(), i.getUpdatedAt(), items);
    }
}

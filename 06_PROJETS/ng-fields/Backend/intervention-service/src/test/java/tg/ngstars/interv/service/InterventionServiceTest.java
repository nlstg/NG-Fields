package tg.ngstars.interv.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tg.ngstars.interv.dto.*;
import tg.ngstars.interv.exception.ForbiddenException;
import tg.ngstars.interv.exception.NotFoundException;
import tg.ngstars.interv.model.Intervention;
import tg.ngstars.interv.model.InterventionItem;
import tg.ngstars.interv.repository.InterventionRepository;

@ExtendWith(MockitoExtension.class)
class InterventionServiceTest {

    @Mock InterventionRepository repo;
    InterventionService service;

    UUID userId = UUID.randomUUID();
    UUID interventionId = UUID.randomUUID();

    Intervention intervention;

    @BeforeEach
    void setUp() {
        service = new InterventionService(repo);
        intervention = Intervention.builder()
                .id(interventionId)
                .reference("INT-001")
                .clientId(UUID.randomUUID())
                .assignedTo(userId)
                .build();
    }

    @Test
    void updateSchedule_shouldSetTimesAndComputeDuration() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var start = OffsetDateTime.parse("2025-01-15T08:00:00Z");
        var end = OffsetDateTime.parse("2025-01-15T10:30:00Z");
        var req = new UpdateScheduleRequest(null, null, start, end);

        var result = service.updateSchedule(interventionId, req, userId, false);

        assertEquals(start, result.startTime());
        assertEquals(end, result.endTime());
        assertEquals(150, result.durationMinutes());
    }

    @Test
    void updateSchedule_whenNotOwner_throwsForbidden() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));

        var otherUser = UUID.randomUUID();
        var req = new UpdateScheduleRequest(null, null, null, null);

        assertThrows(ForbiddenException.class,
                () -> service.updateSchedule(interventionId, req, otherUser, false));
    }

    @Test
    void updateSchedule_adminCanBypassOwnership() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var otherUser = UUID.randomUUID();
        var req = new UpdateScheduleRequest(null, null, null, null);

        assertDoesNotThrow(() -> service.updateSchedule(interventionId, req, otherUser, true));
    }

    @Test
    void getIntervention_notFound_throwsNotFound() {
        when(repo.findById(interventionId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getIntervention(interventionId));
    }

    @Test
    void closeIntervention_withAllSignatures_setsCompleted() {
        intervention.setClientSignature("sig-client");
        intervention.setTechnicianSignature("sig-tech");
        intervention.setManagerSignature("sig-mgr");
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.closeIntervention(interventionId, userId, false);

        assertEquals("COMPLETED", result.status());
        assertNotNull(result.signedAt());
    }

    @Test
    void closeIntervention_withoutSignatures_doesNotComplete() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.closeIntervention(interventionId, userId, false);

        assertNotEquals("COMPLETED", result.status());
    }

    @Test
    void syncFromMobile_newIntervention_createsIt() {
        var localId = "local-123";
        var clientId = UUID.randomUUID();
        when(repo.findByLocalId(localId)).thenReturn(Optional.empty());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var req = new SyncRequest("ref-123", clientId, "Client", null, null, null,
                null, null, null, null, null, "PENDING", OffsetDateTime.now(),
                null, null, localId);

        var result = service.syncFromMobile(req, userId);

        assertEquals(localId, result.localId());
        assertEquals(clientId, result.clientId());
    }

    @Test
    void syncFromMobile_existingIntervention_updatesIt() {
        var localId = "local-123";
        intervention.setLocalId(localId);
        when(repo.findByLocalId(localId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var req = new SyncRequest("ref-123", UUID.randomUUID(), null, "e@mail.com", "123-456", "Addr 1",
                null, null, null, null, null, "COMPLETED", null, null, null, localId);

        var result = service.syncFromMobile(req, userId);

        assertEquals("COMPLETED", result.status());
        assertEquals("e@mail.com", result.clientEmail());
        assertEquals("123-456", result.clientPhone());
        assertEquals("Addr 1", result.clientAddress());
    }

    @Test
    void updateEquipment_shouldSetAllFields() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var req = new UpdateEquipmentRequest("BrandX", "ModelY", "SN123", "Room 1",
                "Won't start", "OP-42", "http://ticket/42");

        var result = service.updateEquipment(interventionId, req, userId, false);

        assertEquals("BrandX", result.equipmentBrand());
        assertEquals("ModelY", result.equipmentModel());
        assertEquals("SN123", result.equipmentSerial());
        assertEquals("Room 1", result.equipmentLocation());
        assertEquals("Won't start", result.reportedIssue());
        assertEquals("OP-42", result.openprojectTicketId());
        assertEquals("http://ticket/42", result.openprojectTicketUrl());
    }

    @Test
    void updateDiagnosis_shouldSetDiagnosisAndWorkDone() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var req = new UpdateDiagnosisRequest("Bad capacitor", "Replaced capacitor");

        var result = service.updateDiagnosis(interventionId, req, userId, false);

        assertEquals("Bad capacitor", result.diagnosis());
        assertEquals("Replaced capacitor", result.workDone());
    }

    @Test
    void updateResult_shouldSetResult() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.updateResult(interventionId, new UpdateResultRequest("COMPLETED"), userId, false);

        assertEquals("COMPLETED", result.result());
    }

    @Test
    void addItem_shouldAddToIntervention() {
        when(repo.findById(interventionId)).thenReturn(Optional.of(intervention));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var req = new ItemRequest("PART", "New capacitor", 2, new BigDecimal("15.00"));

        var result = service.addItem(interventionId, req, userId, false);

        assertEquals(1, result.items().size());
        assertEquals("New capacitor", result.items().getFirst().description());
    }
}

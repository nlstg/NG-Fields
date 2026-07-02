package tg.ngstars.interv.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tg.ngstars.interv.dto.CreateInterventionRequest;
import tg.ngstars.interv.dto.InterventionResponse;
import tg.ngstars.interv.service.InterventionService;
import tg.ngstars.interv.service.SecurityUtils;

@RestController
@RequestMapping("/api/admin/interventions")
@PreAuthorize("hasRole('ADMIN')")
public class InterventionController {

    private final InterventionService interventionService;
    private final SecurityUtils securityUtils;

    public InterventionController(InterventionService interventionService, SecurityUtils securityUtils) {
        this.interventionService = interventionService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<InterventionResponse> createIntervention(
            @Valid @RequestBody CreateInterventionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interventionService.createIntervention(request, securityUtils.getCurrentUserId()));
    }

    @GetMapping
    public ResponseEntity<Page<InterventionResponse>> getInterventions(Pageable pageable) {
        return ResponseEntity.ok(interventionService.getInterventions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterventionResponse> getIntervention(@PathVariable UUID id) {
        return ResponseEntity.ok(interventionService.getIntervention(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterventionResponse> updateIntervention(@PathVariable UUID id,
            @Valid @RequestBody CreateInterventionRequest request) {
        return ResponseEntity.ok(interventionService.updateIntervention(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable UUID id) {
        interventionService.deleteIntervention(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable UUID id) {
        var pdf = interventionService.generatePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=intervention.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<InterventionResponse>> getClientInterventions(
            @PathVariable UUID clientId) {
        return ResponseEntity.ok(interventionService.getClientInterventions(clientId));
    }
}

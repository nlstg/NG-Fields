package tg.ngstars.ng_fields_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tg.ngstars.ng_fields_api.dto.*;
import tg.ngstars.ng_fields_api.service.InterventionService;
import tg.ngstars.ng_fields_api.service.PdfService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interventions")
public class InterventionController {

    private final InterventionService service;
    private final PdfService pdfService;

    public InterventionController(InterventionService service, PdfService pdfService) {
        this.service    = service;
        this.pdfService = pdfService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<InterventionResponse> create(
            @Valid @RequestBody CreateInterventionRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req, jwt));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<InterventionResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateInterventionRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<InterventionResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<List<InterventionResponse>> listMine(
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listByTechnician(jwt.getSubject()));
    }

    @GetMapping("/by-client/{clientId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','TECHNICIAN')")
    public ResponseEntity<List<InterventionResponse>> listByClient(
            @PathVariable UUID clientId) {
        return ResponseEntity.ok(service.listByClient(clientId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InterventionResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/{id}/items")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<InterventionItemResponse> addItem(
            @PathVariable UUID id,
            @Valid @RequestBody InterventionItemRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addItem(id, req));
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InterventionItemResponse>> listItems(@PathVariable UUID id) {
        return ResponseEntity.ok(service.listItems(id));
    }

    @PutMapping("/{id}/items/{itemId}")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<InterventionItemResponse> updateItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId,
            @Valid @RequestBody InterventionItemRequest req) {
        return ResponseEntity.ok(service.updateItem(id, itemId, req));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<Void> deleteItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId) {
        service.deleteItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<byte[]> generatePdf(@PathVariable UUID id) throws IOException {
        byte[] pdf = pdfService.generatePdf(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"rapport-" + id + ".pdf\"")
            .body(pdf);
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID id) throws IOException {
        byte[] pdf = pdfService.generatePdf(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"rapport-" + id + ".pdf\"")
            .body(pdf);
    }
}

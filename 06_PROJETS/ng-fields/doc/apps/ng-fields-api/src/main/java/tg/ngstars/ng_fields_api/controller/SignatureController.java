package tg.ngstars.ng_fields_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.ngstars.ng_fields_api.dto.SignatureRequest;
import tg.ngstars.ng_fields_api.service.SignatureService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/interventions/{id}/signatures")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @PostMapping("/client")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<Map<String, String>> signClient(
            @PathVariable UUID id,
            @Valid @RequestBody SignatureRequest req) throws IOException {
        String url = signatureService.signClient(id, req);
        return ResponseEntity.ok(Map.of(
            "message", "Signature client enregistrée",
            "url", url
        ));
    }

    @PostMapping("/technician")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER','ADMIN')")
    public ResponseEntity<Map<String, String>> signTechnician(
            @PathVariable UUID id,
            @Valid @RequestBody SignatureRequest req) throws IOException {
        String url = signatureService.signTechnician(id, req);
        return ResponseEntity.ok(Map.of(
            "message", "Signature technicien enregistrée",
            "url", url
        ));
    }

    @PostMapping("/manager")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Map<String, String>> signManager(
            @PathVariable UUID id,
            @Valid @RequestBody SignatureRequest req) throws IOException {
        String url = signatureService.signManager(id, req);
        return ResponseEntity.ok(Map.of(
            "message", "Signature manager enregistrée. Intervention validée.",
            "url", url
        ));
    }
}

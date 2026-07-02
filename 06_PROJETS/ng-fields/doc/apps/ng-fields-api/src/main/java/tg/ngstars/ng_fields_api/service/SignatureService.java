package tg.ngstars.ng_fields_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.ngstars.ng_fields_api.dto.SignatureRequest;
import tg.ngstars.ng_fields_api.model.Intervention;
import tg.ngstars.ng_fields_api.model.InterventionStatus;
import tg.ngstars.ng_fields_api.repository.InterventionRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class SignatureService {

    private static final Logger log = LoggerFactory.getLogger(SignatureService.class);

    private final InterventionRepository interventionRepo;
    private final StorageService storageService;

    public SignatureService(
            InterventionRepository interventionRepo,
            StorageService storageService) {
        this.interventionRepo = interventionRepo;
        this.storageService   = storageService;
    }

    public String signClient(UUID interventionId, SignatureRequest req) throws IOException {
        Intervention i = findOrThrow(interventionId);
        String url = storageService.storeBase64(
            req.imageBase64(),
            "signatures/" + interventionId
        );
        i.setSignatureClientUrl(url);
        interventionRepo.save(i);
        log.info("Signature CLIENT enregistrée — intervention {}", interventionId);
        return url;
    }

    public String signTechnician(UUID interventionId, SignatureRequest req) throws IOException {
        Intervention i = findOrThrow(interventionId);
        String url = storageService.storeBase64(
            req.imageBase64(),
            "signatures/" + interventionId
        );
        i.setSignatureTechnicianUrl(url);
        interventionRepo.save(i);
        log.info("Signature TECHNICIEN enregistrée — intervention {}", interventionId);
        return url;
    }

    public String signManager(UUID interventionId, SignatureRequest req) throws IOException {
        Intervention i = findOrThrow(interventionId);
        String url = storageService.storeBase64(
            req.imageBase64(),
            "signatures/" + interventionId
        );
        i.setSignatureManagerUrl(url);

        if (i.getSignatureClientUrl() != null
                && i.getSignatureTechnicianUrl() != null) {
            i.setStatus(InterventionStatus.COMPLETED);
            log.info("Intervention {} → COMPLETED (3 signatures présentes)", interventionId);
        }

        interventionRepo.save(i);
        log.info("Signature MANAGER enregistrée — intervention {}", interventionId);
        return url;
    }

    private Intervention findOrThrow(UUID id) {
        return interventionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Intervention introuvable : " + id));
    }
}

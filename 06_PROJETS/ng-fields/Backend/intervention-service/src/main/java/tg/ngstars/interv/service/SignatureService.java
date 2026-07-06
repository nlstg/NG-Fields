package tg.ngstars.interv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.ngstars.interv.client.MediaClient;
import tg.ngstars.interv.dto.SignatureRequest;
import tg.ngstars.interv.model.Intervention;
import tg.ngstars.interv.repository.InterventionRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class SignatureService {

    private static final Logger log = LoggerFactory.getLogger(SignatureService.class);

    private final InterventionRepository interventionRepo;
    private final MediaClient mediaClient;

    public SignatureService(
            InterventionRepository interventionRepo,
            MediaClient mediaClient) {
        this.interventionRepo = interventionRepo;
        this.mediaClient      = mediaClient;
    }

    public String signClient(UUID interventionId, SignatureRequest req) throws IOException {
        var i = findOrThrow(interventionId);
        String url = mediaClient.uploadBase64(req.imageBase64());
        i.setClientSignature(url);
        interventionRepo.save(i);
        log.info("Signature CLIENT enregistrée — intervention {}", interventionId);
        return url;
    }

    public String signTechnician(UUID interventionId, SignatureRequest req) throws IOException {
        var i = findOrThrow(interventionId);
        String url = mediaClient.uploadBase64(req.imageBase64());
        i.setTechnicianSignature(url);
        interventionRepo.save(i);
        log.info("Signature TECHNICIEN enregistrée — intervention {}", interventionId);
        return url;
    }

    public String signManager(UUID interventionId, SignatureRequest req) throws IOException {
        var i = findOrThrow(interventionId);
        String url = mediaClient.uploadBase64(req.imageBase64());
        i.setManagerSignature(url);

        if (i.getClientSignature() != null
                && i.getTechnicianSignature() != null) {
            i.setStatus("COMPLETED");
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

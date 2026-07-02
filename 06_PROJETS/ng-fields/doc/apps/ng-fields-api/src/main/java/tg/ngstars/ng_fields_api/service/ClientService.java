package tg.ngstars.ng_fields_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tg.ngstars.ng_fields_api.dto.ClientRequest;
import tg.ngstars.ng_fields_api.dto.ClientResponse;
import tg.ngstars.ng_fields_api.model.Client;
import tg.ngstars.ng_fields_api.repository.ClientRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository repo;
    private final AuditService auditService;

    public ClientService(ClientRepository repo, AuditService auditService) {
        this.repo = repo;
        this.auditService = auditService;
    }

    public ClientResponse create(ClientRequest req) {
        Client c = new Client();
        applyRequest(c, req);
        Client saved = repo.save(c);
        log.info("Client créé : {} (id={})", saved.getCompanyName(), saved.getId());
        auditService.log("CREATE", "Client", saved.getId(), Map.of("companyName", saved.getCompanyName()));
        return ClientResponse.from(saved);
    }

    public List<ClientResponse> list() {
        return repo.findByActiveTrue()
            .stream()
            .map(ClientResponse::from)
            .toList();
    }

    public ClientResponse get(UUID id) {
        return repo.findById(id)
            .filter(Client::getActive)
            .map(ClientResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + id));
    }

    public ClientResponse update(UUID id, ClientRequest req) {
        Client c = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + id));
        applyRequest(c, req);
        Client saved = repo.save(c);
        auditService.log("UPDATE", "Client", id, Map.of("companyName", saved.getCompanyName()));
        return ClientResponse.from(saved);
    }

    public void deactivate(UUID id) {
        Client c = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + id));
        c.setActive(false);
        repo.save(c);
        log.info("Client désactivé : {} (id={})", c.getCompanyName(), id);
        auditService.log("DELETE", "Client", id, Map.of("companyName", c.getCompanyName()));
    }

    public List<ClientResponse> search(String q) {
        if (q == null || q.isBlank()) return list();
        return repo.search(q.trim())
            .stream()
            .map(ClientResponse::from)
            .toList();
    }

    private void applyRequest(Client c, ClientRequest req) {
        c.setCompanyName(req.companyName());
        c.setEmail(req.email());
        c.setPhone(req.phone());
        c.setAddress(req.address());
        c.setLatitude(req.latitude());
        c.setLongitude(req.longitude());
        c.setContactName(req.contactName());
        c.setContactPhone(req.contactPhone());
    }
}

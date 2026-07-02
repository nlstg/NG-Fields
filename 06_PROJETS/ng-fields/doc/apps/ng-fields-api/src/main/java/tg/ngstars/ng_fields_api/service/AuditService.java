package tg.ngstars.ng_fields_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.ngstars.ng_fields_api.model.audit.AuditLog;
import tg.ngstars.ng_fields_api.repository.AuditLogRepository;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogRepository auditLogRepo;

    public AuditService(AuditLogRepository auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    public void log(String action, String entityType, UUID entityId, Map<String, Object> changes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actorId = null;
        String actorUsername = null;

        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            actorId = jwt.getSubject();
            actorUsername = jwt.getClaimAsString("preferred_username");
        }

        AuditLog entry = new AuditLog();
        entry.setActorId(actorId);
        entry.setActorUsername(actorUsername);
        entry.setAction(action);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId != null ? entityId.toString() : null);
        entry.setChanges(changes);

        auditLogRepo.save(entry);
        log.debug("Audit: {} {} {} (actor={})", action, entityType, entityId, actorUsername);
    }
}

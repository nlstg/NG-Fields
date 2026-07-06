package tg.ngstars.auth.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tg.ngstars.auth.model.AuditLog;
import tg.ngstars.auth.repository.AuditLogRepository;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock AuditLogRepository auditLogRepository;
    @InjectMocks AuditService service;

    @Test
    void log_shouldSaveAuditLog() {
        service.log(UUID.randomUUID(), "USER_CREATED", "User", "123", "details", "127.0.0.1");
        verify(auditLogRepository).save(any(AuditLog.class));
    }
}

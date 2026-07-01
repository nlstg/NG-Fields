package tg.ngstars.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tg.ngstars.auth.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}

package tg.ngstars.client.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tg.ngstars.client.model.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByEmail(String email);

    Optional<Client> findByReference(String reference);

    Page<Client> findByActiveTrue(Pageable pageable);

    @Query("""
        SELECT c FROM Client c
        WHERE c.active = true
        AND (
            LOWER(c.companyName) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(c.contactName) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :q, '%'))
        )
        """)
    Page<Client> search(@Param("q") String query, Pageable pageable);
}

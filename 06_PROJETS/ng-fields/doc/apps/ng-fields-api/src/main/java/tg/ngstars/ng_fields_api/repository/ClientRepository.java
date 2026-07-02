package tg.ngstars.ng_fields_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.ngstars.ng_fields_api.model.Client;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findByActiveTrue();

    @Query("""
        SELECT c FROM Client c
        WHERE c.active = true
        AND (
            LOWER(c.companyName)    LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(c.email)   LIKE LOWER(CONCAT('%', :q, '%')) OR
            c.phone          LIKE CONCAT('%', :q, '%')
        )
        """)
    List<Client> search(String q);
}

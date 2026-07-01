package tg.ngstars.client.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tg.ngstars.client.model.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findBySiret(String siret);

    boolean existsByEmail(String email);

    boolean existsBySiret(String siret);

    List<Client> findByNameContainingIgnoreCase(String name);

    List<Client> findByActiveTrue();
}

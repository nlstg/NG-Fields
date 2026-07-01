package tg.ngstars.client.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tg.ngstars.client.dto.ClientResponse;
import tg.ngstars.client.dto.CreateClientRequest;
import tg.ngstars.client.model.Client;
import tg.ngstars.client.repository.ClientRepository;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientResponse createClient(CreateClientRequest request) {
        if (clientRepository.existsByEmail(request.email()))
            throw new IllegalArgumentException("Email already exists");
        if (request.siret() != null && clientRepository.existsBySiret(request.siret()))
            throw new IllegalArgumentException("SIRET already exists");

        return toResponse(clientRepository.save(Client.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .city(request.city())
                .postalCode(request.postalCode())
                .country(request.country() != null ? request.country() : "France")
                .siret(request.siret())
                .notes(request.notes())
                .active(true)
                .build()));
    }

    public List<ClientResponse> getClients() {
        return clientRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public ClientResponse getClient(UUID id) {
        return toResponse(clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id)));
    }

    @Transactional
    public ClientResponse updateClient(UUID id, CreateClientRequest request) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));

        if (!client.getEmail().equals(request.email()) && clientRepository.existsByEmail(request.email()))
            throw new IllegalArgumentException("Email already exists");
        if (request.siret() != null && !request.siret().equals(client.getSiret())
                && clientRepository.existsBySiret(request.siret()))
            throw new IllegalArgumentException("SIRET already exists");

        client.setName(request.name());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        client.setAddress(request.address());
        client.setCity(request.city());
        client.setPostalCode(request.postalCode());
        client.setCountry(request.country() != null ? request.country() : "France");
        client.setSiret(request.siret());
        client.setNotes(request.notes());

        return toResponse(clientRepository.save(client));
    }

    @Transactional
    public void deleteClient(UUID id) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
        client.setActive(false);
        clientRepository.save(client);
    }

    public List<ClientResponse> searchClients(String query) {
        return clientRepository.findByNameContainingIgnoreCase(query).stream()
                .map(this::toResponse).toList();
    }

    private ClientResponse toResponse(Client c) {
        return new ClientResponse(c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                c.getAddress(), c.getCity(), c.getPostalCode(), c.getCountry(),
                c.getSiret(), c.getNotes(), c.getActive(), c.getCreatedAt(), c.getUpdatedAt());
    }
}

package tg.ngstars.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tg.ngstars.client.dto.ClientResponse;
import tg.ngstars.client.dto.CreateClientRequest;
import tg.ngstars.client.dto.UpdateClientRequest;
import tg.ngstars.client.exception.ConflictException;
import tg.ngstars.client.exception.NotFoundException;
import tg.ngstars.client.model.Client;
import tg.ngstars.client.repository.ClientRepository;

@Service
@Transactional
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse createClient(CreateClientRequest request, String createdBy) {
        if (clientRepository.existsByEmail(request.email()))
            throw new ConflictException("Un client avec l'email '" + request.email() + "' existe deja");

        var client = Client.builder()
                .reference(generateReference())
                .companyName(request.companyName())
                .contactName(request.contactName())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .createdBy(createdBy)
                .build();

        var saved = clientRepository.save(client);
        log.info("Fiche client creee : {} (ref={})", request.companyName(), saved.getReference());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ClientResponse> listClients(int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("companyName").ascending());
        return clientRepository.findByActiveTrue(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ClientResponse getClient(java.util.UUID id) {
        return clientRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Client introuvable : id=" + id));
    }

    public ClientResponse updateClient(java.util.UUID id, UpdateClientRequest request) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client introuvable : id=" + id));

        if (!client.getEmail().equals(request.email()) && clientRepository.existsByEmail(request.email()))
            throw new ConflictException("L'email '" + request.email() + "' est deja utilise");

        client.setCompanyName(request.companyName());
        client.setContactName(request.contactName());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        client.setAddress(request.address());
        client.setLatitude(request.latitude());
        client.setLongitude(request.longitude());

        return toResponse(clientRepository.save(client));
    }

    public void deactivateClient(java.util.UUID id) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client introuvable : id=" + id));
        client.setActive(false);
        clientRepository.save(client);
        log.info("Client desactive : {}", client.getCompanyName());
    }

    @Transactional(readOnly = true)
    public Page<ClientResponse> searchClients(String query, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("companyName").ascending());
        return clientRepository.search(query, pageable).map(this::toResponse);
    }

    private String generateReference() {
        long count = clientRepository.count() + 1;
        return String.format("CLT-%04d", count);
    }

    private ClientResponse toResponse(Client c) {
        return new ClientResponse(c.getId(), c.getReference(), c.getCompanyName(),
                c.getContactName(), c.getEmail(), c.getPhone(), c.getAddress(),
                c.getLatitude(), c.getLongitude(), c.getActive(), c.getCreatedAt());
    }
}

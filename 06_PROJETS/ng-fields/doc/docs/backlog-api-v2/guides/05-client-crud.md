# 05 — CRUD Clients

**Objectif :** API REST pour gérer les clients (création, consultation, modification, désactivation)
**Dépend de :** [04-user-registration.md](04-user-registration.md)

---

## Endpoints

| Méthode | Endpoint | Rôle | Description |
|---------|----------|------|-------------|
| POST | `/api/clients` | ADMIN | Créer un client |
| GET | `/api/clients` | ADMIN, MANAGER, TECHNICIAN | Lister les clients |
| GET | `/api/clients/{id}` | ADMIN, MANAGER, TECHNICIAN | Détail d'un client |
| PUT | `/api/clients/{id}` | ADMIN | Modifier un client |
| DELETE | `/api/clients/{id}` | ADMIN | Désactiver un client |
| GET | `/api/clients/search?q=` | Tous | Rechercher un client |

## Fichiers à créer

| Fichier | Description |
|---------|-------------|
| `model/Client.java` | Entité JPA |
| `repository/ClientRepository.java` | Spring Data JPA |
| `dto/CreateClientRequest.java` | DTO création |
| `dto/ClientResponse.java` | DTO réponse |
| `service/ClientService.java` | Logique métier |
| `controller/ClientController.java` | Endpoints REST |

## Guide détaillé

> **À rédiger après validation de la création de compte (Sprint 1b)**

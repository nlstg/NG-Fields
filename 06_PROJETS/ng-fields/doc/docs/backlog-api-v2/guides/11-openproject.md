# 11 — Intégration OpenProject

**Objectif :** Créer et mettre à jour des tickets OpenProject via l'API REST v3
**Dépend de :** [04-user-registration.md](04-user-registration.md)

---

## Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/client-portal/requests` | Soumettre une demande (public) |
| POST | `/api/admin/openproject/tickets` | Créer un ticket OP (ADMIN) |

## Méthodes OpenProjectService

| Méthode | Description |
|---------|-------------|
| `createTicket(workPackageRequest)` | Crée un work package |
| `updateTicket(id, status)` | Met à jour le statut |
| `getTicket(id)` | Consulte un ticket |

## Guide détaillé

> **À rédiger après Sprint 4 — se référer à [docs/openproject-api.md](../openproject-api.md) pour les détails de l'API**

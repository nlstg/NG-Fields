# 08 — Signatures Électroniques

**Objectif :** Enregistrement des signatures (client, technicien, manager) sur les fiches d'intervention
**Dépend de :** [06-intervention-crud.md](06-intervention-crud.md)

---

## Endpoints

| Méthode | Endpoint | Rôle | Description |
|---------|----------|------|-------------|
| POST | `/api/interventions/{id}/signatures/client` | TECHNICIAN | Signature client |
| POST | `/api/interventions/{id}/signatures/technician` | TECHNICIAN | Signature technicien |
| POST | `/api/interventions/{id}/signatures/manager` | MANAGER, ADMIN | Signature manager (différée) |

## Guide détaillé

> **À rédiger après Sprint 3**

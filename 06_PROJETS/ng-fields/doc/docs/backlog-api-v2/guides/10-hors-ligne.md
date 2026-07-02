# 10 — Mode Hors-ligne et Synchronisation

**Objectif :** Permettre la création d'interventions hors-ligne et la synchronisation au retour de connectivité
**Dépend de :** [06-intervention-crud.md](06-intervention-crud.md)

---

## Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/sync/interventions` | Synchroniser un batch de fiches |

## Stratégie

- Identifiant `local_id` (UUID) côté mobile
- Déduplication : `last-write-wins` sur `updated_at`
- Erreur partielle : les fiches valides sont traitées même si d'autres échouent

## Guide détaillé

> **À rédiger après Sprint 3**

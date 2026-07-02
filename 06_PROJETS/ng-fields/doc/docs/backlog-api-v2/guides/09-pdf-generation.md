# 09 — Génération PDF

**Objectif :** Génération automatique du rapport d'intervention au format PDF
**Dépend de :** [07-photos-storage.md](07-photos-storage.md), [08-signatures.md](08-signatures.md)

---

## Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/interventions/{id}/pdf` | Déclencher/régénérer le PDF |
| GET | `/api/interventions/{id}/pdf` | URL signée du PDF |

## Contenu du PDF

- Logo NG-STARs
- Identifiant unique d'intervention
- 8 sections complètes
- Photos avant/après (miniatures)
- Signatures (images)
- QR code → fiche numérique
- Date et horodatage

## Guide détaillé

> **À rédiger après Sprint 3**

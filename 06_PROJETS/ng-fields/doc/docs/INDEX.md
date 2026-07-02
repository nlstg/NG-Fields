# Documentation Projet NG-Fields

## Structure

```
docs/
├── architecture/           Stack, flux, ADR
├── business/               Processus métier
├── database/               Modèle de données
├── mobile/                 Guide Flutter
├── integrations/           OpenProject, Twilio
├── tests/                  Postman
└── references/             Docs techniques
```

## Documents racine

| Document | Contenu |
|----------|---------|
| `Backlog.md` | Backlog produit (V0/V0.1/V1) |
| `Roadmap.md` | Planning des versions |
| `Objectifs.md` | Objectifs SMART et KPIs |
| `Technologies.md` | Stack technique détaillée |
| `Setup.md` | Guide d'installation |
| `README.md` | Présentation du projet |

## Architecture

| Document | Emplacement |
|----------|-------------|
| Stack technique | `Technologies.md` (racine) |
| Schéma BDD | `infra/supabase/schema.sql` |
| Données de test | `infra/supabase/seed.sql` |
| Config Keycloak | `infra/keycloak/realm-export.json` |
| Docker Compose | `infra/docker-compose.yml` |

## Intégrations

| Intégration | Documentation |
|-------------|---------------|
| OpenProject (API REST v3) | `docs/integrations/openproject-api.md` |
| Keycloak | `docs/references/keycloak-reference.md` |
| Twilio WhatsApp | `docs/integrations/twilio-whatsapp.md` |

## Mobile (Flutter)

| Document | Emplacement |
|----------|-------------|
| Guide Flutter | `docs/mobile/flutter-reference.md` |
| Wireframes | `wireframes/` |
| Guide UI | `docs/mobile/guide-ui.md` |

## Charte graphique NG-STARs

- Logo HD (PNG/SVG) — *à fournir*
- Couleurs corporate — *à fournir*
- Typographie — *à fournir*

---

_Version 2.0 — 03/06/2026_

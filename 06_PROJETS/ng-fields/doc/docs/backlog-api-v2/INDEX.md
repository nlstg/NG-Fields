# Backlog v2 — NG-Fields

**Période :** 1–30 juin 2026
**Contrainte :** Pas de Docker — exécution native uniquement
**Objectif critique :** Créer un compte utilisateur avant le 2 juin au soir

---

## Prérequis — Base de données

Avant tout sprint, configurer PostgreSQL selon l'architecture définie :

| # | Livrable | Référence |
|---|----------|-----------|
| — | Architecture globale (3 bases, rôles, connexion) | [docs/database/database-model.md](../database/database-model.md) |
| — | Base `ng_fields` (schémas, tables, DDL) | [docs/database/ng-fields-application.md](../database/ng-fields-application.md) |
| — | Base `keycloak` (driver, vars d'env, démarrage) | [docs/database/keycloak.md](../database/keycloak.md) |

## Sprint 0  — Setup environnement

| # | Livrable | Guide | Statut |
|---|----------|-------|--------|
| 0.1 | Keycloak 26.6.2 tourne sur `localhost:8080` | [01-setup-keycloak.md](01-setup-keycloak.md) | |
| 0.2 | Realm `ng-fields` + clients OIDC + rôles importés | [02-configure-realm.md](02-configure-realm.md) | |
| 0.3 | Spring Boot démarre et valide les JWT Keycloak | [03-spring-security.md](03-spring-security.md) | |

## Sprint 1a — API publique + auth

| # | Livrable | Guide |
|---|----------|-------|
| 1.1 | `GET /api/public/health` répond 200 | [03-spring-security.md](03-spring-security.md) |
| 1.2 | `GET /api/admin/me` retourne le profil JWT | [04-user-registration.md](04-user-registration.md) |

## Sprint 1b  — CRÉATION DE COMPTE

| #   | Livrable                                                                  | Guide                                                     |
| --- | ------------------------------------------------------------------------- | --------------------------------------------------------- |
| 1.3 | `POST /api/admin/users` crée un utilisateur dans Keycloak                 | [04-user-registration.md](04-user-registration.md) |
| 1.4 | `POST /api/public/register` inscription self-service (rôle CLIENT_PORTAL) | [04-user-registration.md](04-user-registration.md) |
| 1.5 | ✅ Test complet : inscription → login OIDC → API protégée                  |                                                           |

## Sprint 2 — Clients + Interventions

| # | Livrable | Guide |
|---|----------|-------|
| 2.1 | CRUD Clients | [05-client-crud.md](05-client-crud.md) |
| 2.2 | CRUD Interventions (création + 8 sections) | [06-intervention-crud.md](06-intervention-crud.md) |
| 2.3 | Pièces et consommables | [06-intervention-crud.md](06-intervention-crud.md) |

## Sprint 3 — Photos, Signatures, PDF

| # | Livrable | Guide |
|---|----------|-------|
| 3.1 | Upload photos avant/après | [07-photos-storage.md](07-photos-storage.md) |
| 3.2 | Signatures électroniques (3 zones) | [08-signatures.md](08-signatures.md) |
| 3.3 | Génération PDF | [09-pdf-generation.md](09-pdf-generation.md) |

## Sprint 4 — Hors-ligne, OpenProject, Dashboard

| # | Livrable | Guide |
|---|----------|-------|
| 4.1 | Sync offline | [10-hors-ligne.md](10-hors-ligne.md) |
| 4.2 | Intégration OpenProject | [11-openproject.md](11-openproject.md) |
| 4.3 | Dashboard stats | [12-dashboard.md](12-dashboard.md) |

---

## Arborescence des guides

```
docs/
├── backlog-v2/
│   ├── INDEX.md               ← Vous êtes ici
│   └── guides/
│       ├── 01-setup-keycloak.md
│       ├── ...
│       └── 12-dashboard.md
└── database/                  ← Modèle de données PostgreSQL
    ├── database-model.md      ← Architecture globale (3 bases, rôles)
    ├── ng-fields-application.md ← Base ng_fields (schémas, tables, DDL)
    └── keycloak.md            ← Base keycloak (connexion, setup)
```

## Comment lire

1. Suivre les guides dans l'ordre numérique
2. Chaque guide est autonome : prérequis → étapes → commandes → test
3. Le Sprint 1b est la priorité absolue : création de compte

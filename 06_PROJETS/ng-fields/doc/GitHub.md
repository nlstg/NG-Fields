# GitHub — Configuration NG-Fields

## 1. Dépôt

| Champ | Valeur |
|-------|--------|
| URL | `https://github.com/moltesse12/ng-fields` |
| Visibilité | Public |
| Description | Digitalisation de la gestion des interventions terrain — Spring Boot + Flutter + Angular |

## 2. Stack

| Couche | Technologie |
|--------|-------------|
| Backend | Spring Boot 4.0.6 / Java 25 |
| Auth | Keycloak 26.6.2 (OAuth2 / OIDC) |
| Mobile | Flutter / Dart |
| Web | Angular / TypeScript |
| Database | PostgreSQL 16 (Supabase) |
| Migrations | Flyway |
| Queue | Redis |
| CI/CD | GitHub Actions |

## 3. Branches

| Branche | Rôle |
|---------|------|
| `main` | Production |
| `develop` | Intégration |
| `feature/US-*` | Fonctionnalités |
| `fix/US-*` | Corrections |
| `release/v*.*` | Releases |

## 4. Secrets GitHub

| Secret | Description |
|--------|-------------|
| `KEYCLOAK_URL` | URL du serveur Keycloak |
| `KEYCLOAK_REALM` | Realm (ng-fields) |
| `KEYCLOAK_CLIENT_ID` | Client ID backend |
| `KEYCLOAK_CLIENT_SECRET` | Client Secret backend |
| `DATABASE_URL` | URL PostgreSQL (Supabase) |
| `SUPABASE_URL` | URL projet Supabase |
| `SUPABASE_ANON_KEY` | Clé anon Supabase |
| `SENDGRID_API_KEY` | Clé API SendGrid |
| `OPENPROJECT_API_TOKEN` | Token API OpenProject (V1) |
| `FIREBASE_SERVICE_ACCOUNT` | JSON Firebase (V1) |

## 5. Workflows CI/CD

| Fichier | Rôle |
|---------|------|
| `.github/workflows/backend.yml` | CI Maven (lint → tests → build JAR) |
| `.github/workflows/mobile.yml` | CI Flutter (compile) |
| `.github/workflows/web.yml` | CI Angular (à créer en V1) |

---

_Version 2.0 — 03/06/2026_

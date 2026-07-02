---
tags:
  - projet
  - ng-fields
  - technologies
created: 2026-06-03
status: v2
---

# Stack Technique — NG-Fields

## Stack retenue

| Couche | Technologie | Version | Usage |
|--------|-------------|---------|-------|
| Backend | Spring Boot | 4.0.6 | API REST |
| Langage backend | Java | 25 | — |
| Build | Maven | Wrapper | — |
| ORM | Spring Data JPA + Hibernate | — | Persistance |
| Auth | Keycloak (OAuth2/OIDC) | 26.6.2 | SSO + RBAC |
| API Docs | SpringDoc OpenAPI | — | Swagger UI |
| PDF | OpenPDF | 1.4.1 | Génération rapports |
| QR Code | ZXing | 3.5.3 | QR dans les PDF |
| Base de données | PostgreSQL | 16 | via Supabase |
| Migrations | Flyway | — | Versioning schéma |
| Cache / Queue | Redis | 7+ | Async queue |
| Mobile | Flutter | 3.x | App terrain |
| Langage mobile | Dart | 3.x | — |
| State management | Riverpod | — | Flutter |
| Navigation | GoRouter | — | Flutter |
| Base locale | Drift (SQLite) | — | Mode hors-ligne |
| Web | Angular | 18+ | Dashboard manager |
| CI/CD | GitHub Actions | — | Pipeline |
| Monitoring | Sentry | free | Errors |
| Storage | Supabase Storage | — | Photos, PDF, signatures |

---

## Stack Mobile — Flutter

| Composant | Technologie |
|-----------|-------------|
| Framework | Flutter 3.x |
| Langage | Dart 3.x |
| State Management | Riverpod |
| Navigation | GoRouter |
| UI | Material 3 (Android) + Cupertino (iOS) |
| Base locale (offline) | Drift (SQLite) |
| HTTP Client | Dio |
| Signature | signature |
| Camera | image_picker |
| GPS | geolocator |
| Secure storage | flutter_secure_storage |
| Notifications | Firebase Cloud Messaging |

---

## Stack Web — Angular

| Composant | Technologie |
|-----------|-------------|
| Framework | Angular 18+ |
| Langage | TypeScript |
| UI | Angular Material |
| Graphiques | Chart.js ou ngx-charts |
| HTTP Client | HttpClient (Angular) |
| Auth | OIDC Client (angular-auth-oidc-client) |

---

## Stack Backend — Spring Boot

| Composant | Technologie |
|-----------|-------------|
| Framework | Spring Boot 4.0.6 |
| Runtime | Java 25 |
| Build | Maven |
| ORM | Spring Data JPA + Hibernate |
| Auth | Spring Security + OAuth2 Resource Server |
| Migrations | Flyway |
| Validation | Jakarta Validation + Hibernate Validator |
| Documentation | SpringDoc OpenAPI (Swagger) |
| PDF | OpenPDF + ZXing |
| Queue | Redis (Redisson/Spring Data Redis) |
| Email | Spring Mail (JavaMail) + SendGrid |
| WhatsApp | Meta Cloud API (Twilio) |
| OpenProject | API REST v3 |
| Monitoring | Spring Actuator + Micrometer + Prometheus |
| Logs | Logback + logstash-logback-encoder (JSON) |

---

## Base de Données

| Composant | Technologie |
|-----------|-------------|
| Primary | PostgreSQL 16 via Supabase (free tier) |
| Cache / Queue | Redis 7+ (local Docker) |
| Files | Supabase Storage (S3-compatible, 1 Go free) |
| Mobile offline | Drift (SQLite) |

---

## Infrastructure

| Composant | Technologie |
|-----------|-------------|
| Database | Supabase Cloud (free tier) |
| Storage | Supabase Storage |
| CI/CD | GitHub Actions |
| Monitoring | Sentry (free tier) |
| Backup | Supabase backup auto |

---

## Budget

| Poste | Coût |
|-------|------|
| Supabase (free tier) | 0 € |
| GitHub (free) | 0 € |
| SendGrid (Free) | 0 € |
| Twilio WhatsApp | À l'usage |
| Sentry (Free) | 0 € |
| **Total projet** | **0 €** |

---

## Sécurité

| Exigence | Implémentation |
|----------|----------------|
| HTTPS | TLS 1.3 (Let's Encrypt / Caddy) |
| Auth | JWT + Refresh Token + RBAC (Keycloak) |
| BDD | TLS en transit (Supabase) |
| Offline | Chiffrement local (Drift encrypt) |
| Audit trail | Logs applicatifs + table `audit_logs` |
| RGPD | Consentement + droit effacement + registre |

---

_Version 2.0 — 03/06/2026_

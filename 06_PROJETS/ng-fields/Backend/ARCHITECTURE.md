# Architecture du Backend NG-Fields

## 1. Présentation générale

Le backend NG-Fields est une architecture **microservices** (Spring Boot 4.1.0 / Java 25) sécurisée par **Keycloak** (OAuth2/JWT), avec une **API Gateway** comme point d'entrée unique et une base de données **PostgreSQL** avec un schéma dédié par service.

### Stack technique

| Composant | Technologie | Version |
|---|---|---|
| Runtime | Java | 25 |
| Framework | Spring Boot | 4.1.0 |
| Gateway | Spring Cloud Gateway (WebFlux) | 2025.1.2 |
| Auth | Keycloak (OAuth2/JWT) | 26.0.9 |
| Base de données | PostgreSQL | — |
| ORM | JPA / Hibernate | — |
| Migrations | Flyway | — |
| API Docs | SpringDoc OpenAPI | 3.0.3 |
| PDF | OpenPDF (LibrePDF) | 1.4.1 |
| Rate limiting | Redis + Resilience4j | — |
| Tests | JUnit 5 + Mockito | — |

---

## 2. Architecture des services

```
┌─────────────────────────────────────────────────────┐
│                   Frontend                           │
│        Angular (port 4200) / Mobile (port 8100)     │
└──────────────────────┬──────────────────────────────┘
                       │ HTTPS / JWT
                       ▼
┌─────────────────────────────────────────────────────┐
│              API Gateway (port 8080)                  │
│    Spring Cloud Gateway (WebFlux réactif)             │
│    Routage + Rate limiting (Redis) + CORS            │
└──┬───────┬───────┬───────┬───────┬───────┬──────────┘
   │       │       │       │       │       │
   ▼       ▼       ▼       ▼       ▼       ▼
┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐
│ Auth │ │Client│ │ Inter│ │ Media│ │ Notif│ │Report│
│ 8081 │ │ 8082 │ │ 8083 │ │ 8084 │ │ 8085 │ │ 8086 │
└──┬───┘ └──┬───┘ └──┬───┘ └──────┘ └──────┘ └──────┘
   │        │        │
   ▼        ▼        ▼
┌──────────────────────────────┐
│   PostgreSQL (port 5432)      │
│   Schémas: auth, client,     │
│   intervention, notification │
└──────────────────────────────┘

     ┌──────────────────┐
     │    Keycloak      │
     │   (port 8088)    │
     │ realm: ng-fields │
     └──────────────────┘
```

### Accès direct aux services (dev uniquement)

Chaque service expose son propre port et son propre SecurityConfig. En développement, on peut appeler directement `localhost:8081`, `8082`, etc. En production, seul le port 8080 (gateway) doit être exposé.

---

## 3. Détail des services

### 3.1 API Gateway — Port 8080

**Rôle** : Point d'entrée unique, routage, rate limiting, CORS, agrégation Swagger.

**Routes configurées** :

| Route | Cible | Rate limité |
|---|---|---|
| `/api/public/**` → Auth (8081) | Inscription, health | Non |
| `/api/admin/users/**` → Auth (8081) | Admin users | Oui (10 req/s) |
| `/api/users/me` → Auth (8081) | Profil | Non |
| `/api/clients/**` → Client (8082) | Clients CRUD | Oui (20 req/s) |
| `/api/interventions/**` → Intervention (8083) | Interventions | Oui (30 req/s) |
| `/api/sync/**` → Intervention (8083) | Sync mobile | Non |
| `/api/media/**` → Media (8084) | Fichiers | Non |

**Rate limiting** : Redis + Resilience4j circuit breaker (dépendance présente mais circuits non configurés).

### 3.2 Auth Service — Port 8081

**Rôle** : Gestion des utilisateurs, rôles, authentification (délégation Keycloak), audit trail.

**Tables** : `users` (copie locale des utilisateurs Keycloak), `audit_logs`.

**Endpoints** :

| Méthode | Chemin | Accès | Description |
|---|---|---|---|
| POST | `/api/public/register` | Public | Auto-inscription (force rôle CLIENT_PORTAL) |
| GET | `/api/public/health` | Public | Health check |
| GET | `/api/users/me` | Auth | Profil utilisateur courant |
| PUT | `/api/users/me` | Auth | Mise à jour profil |
| POST | `/api/admin/users` | ADMIN | Création utilisateur |
| GET | `/api/admin/users` | ADMIN | Liste utilisateurs |
| GET | `/api/admin/users/{id}` | ADMIN | Détail utilisateur |
| PUT | `/api/admin/users/{id}` | ADMIN | Modification |
| DELETE | `/api/admin/users/{id}` | ADMIN | Désactivation |
| PATCH | `/api/admin/users/{keycloakId}/roles` | ADMIN | Changement rôle |
| PATCH | `/api/admin/users/{keycloakId}/status` | ADMIN | Activer/désactiver |
| POST | `/api/admin/users/{keycloakId}/reset-password` | ADMIN | Réinitialisation mot de passe |

**Rôles** : `ADMIN`, `MANAGER`, `TECHNICIAN`, `CLIENT_PORTAL`.

**Particularité** : Toute opération utilisateur (création, modification, désactivation, changement rôle) est synchronisée entre Keycloak et la base locale, avec une trace d'audit.

### 3.3 Client Service — Port 8082

**Rôle** : Gestion des fiches clients.

**Table** : `clients`.

**Endpoints** :

| Méthode | Chemin | Accès | Description |
|---|---|---|---|
| POST | `/api/clients` | ADMIN | Création client |
| GET | `/api/clients` | ADMIN/MANAGER/TECHNICIAN | Liste paginée |
| GET | `/api/clients/search?q=` | ADMIN/MANAGER/TECHNICIAN | Recherche (nom, contact, email) |
| GET | `/api/clients/{id}` | ADMIN/MANAGER/TECHNICIAN | Détail client |
| PUT | `/api/clients/{id}` | ADMIN | Modification |
| DELETE | `/api/clients/{id}` | ADMIN | Désactivation (soft delete) |

### 3.4 Intervention Service — Port 8083

**Rôle** : Cœur métier — gestion complète des interventions terrain.

**Tables** : `interventions`, `intervention_items`, `intervention_photos`.

**Endpoints** :

| Méthode | Chemin | Accès | Description |
|---|---|---|---|
| POST | `/api/interventions` | Admin/Manager/Technicien | Création intervention |
| GET | `/api/interventions` | Admin/Manager/Technicien | Liste (filtrable par `status`, `technicianId`) |
| GET | `/api/interventions/{id}` | Admin/Manager/Technicien | Détail |
| PUT | `/api/interventions/{id}` | Admin/Manager/Technicien | Mise à jour complète |
| DELETE | `/api/interventions/{id}` | Admin/Manager/Technicien | Désactivation |
| GET | `/api/interventions/{id}/pdf` | Admin/Manager/Technicien | Génération PDF |
| GET | `/api/interventions/by-client/{clientId}` | Admin/Manager/Technicien | Interventions d'un client |
| PATCH | `/api/interventions/{id}/schedule` | Propriétaire* | Horaires |
| PATCH | `/api/interventions/{id}/equipment` | Propriétaire* | Équipement |
| PATCH | `/api/interventions/{id}/diagnosis` | Propriétaire* | Diagnostic |
| PATCH | `/api/interventions/{id}/result` | Propriétaire* | Résultat |
| PATCH | `/api/interventions/{id}/recommendations` | Propriétaire* | Recommandations |
| PATCH | `/api/interventions/{id}/billing` | Propriétaire* | Facturation |
| POST | `/api/interventions/{id}/items` | Propriétaire* | Ajout pièce |
| PUT | `/api/interventions/{id}/items/{itemId}` | Propriétaire* | Modif pièce |
| DELETE | `/api/interventions/{id}/items/{itemId}` | Propriétaire* | Suppr pièce |
| POST | `/api/interventions/{id}/close` | Propriétaire* | Clôture (auto si 3 signatures) |
| POST | `/api/sync/interventions` | Admin/Manager/Technicien | Sync mobile (création ou MAJ) |

\* *Propriétaire = technicien assigné, sauf ADMIN/MANAGER qui peuvent modifier toute intervention.*

**Champs de l'entité Intervention** :

- Informations client (nom, email, téléphone, adresse)
- Équipement (type, marque, modèle, série, localisation)
- Ticket OpenProject (id, URL)
- Diagnostic, travail effectué
- Statut (PENDING → COMPLETED)
- Horaires (départ, arrivée, début, fin, durée calculée)
- Résultat, recommandations
- Facturation (facturable, montant, notes)
- 3 signatures (client, technicien, responsable)
- Photos (avant/après, 5 max chaque)
- Pièces utilisées (type, description, quantité, prix unitaire)
- Synchronisation mobile (local_id)

### 3.5 Media Service — Port 8084

**Rôle** : Stockage et distribution de fichiers (photos, signatures, PDF).

**Particularité** : Service sans base de données — stockage sur disque local (`./uploads`). Les fichiers sont nommés avec UUID pour éviter les collisions.

**Endpoints** :

| Méthode | Chemin | Description |
|---|---|---|
| POST | `/api/media/upload` | Upload fichier (multipart) |
| POST | `/api/media/upload-base64` | Upload image base64 |
| GET | `/api/media/{filename}` | Téléchargement (inline) |
| DELETE | `/api/media/{filename}` | Suppression |

**Sécurité** : Vérification anti-path-traversal (`file.startsWith(uploadPath)`). Pas de validation MIME du contenu réel.

### 3.6 Notification Service — Port 8085

**Statut** : SQUELETTE — non implémenté.

**Prévu** : Envoi d'emails (Mail + Thymeleaf), notifications in-app, SMS.
Dépendances déjà dans le pom.xml : `spring-boot-starter-mail`, `spring-boot-starter-thymeleaf`.

### 3.7 Report Service — Port 8086

**Statut** : SQUELETTE — non implémenté.

**Prévu** : Génération de rapports (CSV en premier lieu, puis PDF).

---

## 4. Sécurité

### Authentification

- **Keycloak** (port 8088) comme fournisseur OAuth2
- Realm : `ng-fields`
- Clients : `ng-fields-frontend` (frontend Angular/Mobile), `ng-fields-backend` (admin API)
- JWT avec claim `realm_access.roles` → mappé en `ROLE_*` Spring Security

### Rôles et permissions

| Rôle | Lectures | Écritures |
|---|---|---|
| ADMIN | Toutes | Toutes |
| MANAGER | Toutes | Toutes sauf admin users |
| TECHNICIAN | Interventions assignées, clients | Interventions assignées uniquement |
| CLIENT_PORTAL | Profil, interventions client | Profil uniquement |

### Flux d'authentification

```
Client → Gateway → [JWT] → Service → Keycloak (validation JWT)
                  ↓
            Rôles extraits du JWT → @PreAuthorize
```

### Configuration CORS

Origines autorisées : `http://localhost:4200` (Angular), `http://localhost:8100` (Mobile).

---

## 5. Base de données

### Schémas

| Schéma | Service | Tables |
|---|---|---|
| `auth` | Auth | `users`, `audit_logs` |
| `client` | Client | `clients` |
| `intervention` | Intervention | `interventions`, `intervention_items`, `intervention_photos` |
| `notification` | Notification | (à créer) |

### Migrations Flyway

Chaque service gère ses propres migrations dans `src/main/resources/db/migration/`.

| Service | Migrations |
|---|---|
| Auth | V1 : init (users, audit_logs) |
| Client | V1 : init (clients) |
| Intervention | V1 : init, V2 : photos+signatures, V3 : sections horaires/résultat/facturation/sync |
| Notification | (à créer) |

---

## 6. Déploiement

### Dépendances externes

| Service | Dépend |
|---|---|
| Tous | PostgreSQL, Keycloak |
| Gateway | Redis (rate limiting) |
| Tous | — |

### Ports

| Service | Port | Profil |
|---|---|---|
| Gateway | 8080 | — |
| Auth | 8081 | — |
| Client | 8082 | — |
| Intervention | 8083 | — |
| Media | 8084 | — |
| Notification | 8085 | Squelette |
| Report | 8086 | Squelette |
| Keycloak | 8088 | Externe |
| PostgreSQL | 5432 | Externe |
| Redis | 6379 | Externe |

---

## 7. Tests

### Couverture actuelle

| Service | Tests | Méthode | Statut |
|---|---|---|---|
| Auth | UserServiceTest (11) + AuditServiceTest (1) | Mockito | ✅ |
| Client | ClientServiceTest (10) | Mockito | ✅ |
| Gateway | KeycloakJwtAuthenticationConverterTest (4) | JUnit | ✅ |
| Intervention | InterventionServiceTest (12) | Mockito | ✅ |
| Notification | contextLoads (1) | Spring Boot | ✅ |
| Report | contextLoads (1) | Spring Boot | ✅ |
| **Total** | **40 tests** | — | **0 échec** |

### Ce qui reste à tester

- Tests d'intégration avec base de données (Testcontainers)
- Tests bout-en-bout via le gateway
- Tests de sécurité (rôles, permissions)

---

## 8. État d'avancement

| Service | Statut | Remarques |
|---|---|---|
| Gateway | ✅ Fonctionnel | Rate limiting actif, manque circuit breaker |
| Auth | ✅ Fonctionnel | Keycloak synchronisé, audit trail |
| Client | ✅ Fonctionnel | CRUD complet, recherche, soft delete |
| Intervention | ✅ Fonctionnel | Tous endpoints métier implémentés |
| Media | ✅ Fonctionnel | Stockage fichier, upload/download/suppression |
| Notification | ⏳ Squelette | Mail + Thymeleaf prêts dans pom.xml |
| Report | ⏳ Squelette | Aucune implémentation |
| Tests | ✅ 40 tests | Auth, Client, Gateway, Intervention |

---

## 9. Décisions techniques

| Décision | Choix | Justification |
|---|---|---|
| Communication inter-services | RestClient | Pas de Feign (complexité inutile pour 5 services) |
| IDs | UUID | Scalable, pas de séquence, merge-friendly |
| Timestamps | OffsetDateTime | Timezone-aware, standard ISO 8601 |
| DTOs | Java records | Immutables, concis, `@Valid` natif |
| Synchro Keycloak/DB | Manuelle dans UserService | Pattern "write-through" |
| PDF | OpenPDF (LibrePDF) | Léger, pas de dépendance lourde |
| Migrations | Flyway | Versionné, réversible |
| Tests | JUnit 5 + Mockito | Standard Spring Boot |

---

## 10. Prochaines étapes

1. **Notification service** — Implémentation complète (email, SMS, in-app)
2. **Report service** — Génération CSV/PDF
3. **Tests d'intégration** — Testcontainers pour tests avec vraie DB
4. **Circuit breaker** — Configurer Resilience4j sur les routes gateway
5. **Média** — Migrer vers Supabase Storage / MinIO S3
6. **Séparation DB** — Utilisateurs PostgreSQL distincts par service
7. **Documentation API** — Swagger agrégé via gateway

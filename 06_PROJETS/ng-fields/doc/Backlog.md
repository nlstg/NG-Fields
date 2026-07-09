---
tags:
  - projet
  - ng-fields
  - backlog
created: 2026-06-03
status: v4.0
---

# Backlog Produit — NG-Fields

**Projet :** NG-Fields — Digitalisation de la gestion des interventions terrain
**Période :** 1 juin — 17 juillet 2026
**Méthodologie :** Agile Scrum — Sprints hebdomadaires
**Stack :** Spring Boot 4.0.6 / Java 25 + Angular 21 / TypeScript + Flutter 3.x / Dart + Keycloak 26.6.2 + Supabase
**Rédigé par :** FOLLY Nelson Emmanuel
**Validateur :** David KATOH
**Version :** 4.0 — 02/07/2026

---

## Légende

| Symbole | Signification |
|---------|---------------|
| 🔴 | Priorité CRITIQUE (bloquante) |
| 🟠 | Priorité HAUTE |
| 🟡 | Priorité MOYENNE |
| 🟢 | Priorité BASSE |
| SP | Story Points (Fibonacci : 1, 2, 3, 5, 8, 13) |
| 🔴 PENDING | Pas commencé |
| 🟡 IN_PROGRESS | En cours |
| 🟢 COMPLETED | Terminé |
| ⚫ BLOCKED | Bloqué |
| [API] | Sous-tâche backend Spring Boot |
| [WEB] | Sous-tâche Angular |
| [MOBILE] | Sous-tâche Flutter |

---

## Workflow Git

### Branches

| Branche | Usage | Créée depuis |
|---------|-------|-------------|
| `main` | Production | — |
| `develop` | Intégration continue | `main` |
| `feature/VX-SY` | Sprint (ex: `feature/V0-S1`) | `develop` |
| `feature/US-NNN` | US spécifique (optionnel) | `feature/VX-SY` |
| `fix/US-NNN` | Correction | `develop` |
| `release/v*.*` | Release | `develop` |

### Conventions de commit

```
type(US-NNN): description en français

Types : feat, fix, docs, refactor, test, chore

Exemples :
feat(US-005): configuration Spring Security OAuth2
fix(US-009): correction validation email client
docs(US-033): documentation Swagger des endpoints
```

### Règles

1. **Commiter après chaque US complétée** (ne pas accumuler)
2. **Push quotidien** en fin de journée, même si la US n'est pas finie
3. **Créer la branche de sprint** au début de chaque sprint
4. **Faire une Pull Request** vers `develop` à la fin de chaque sprint
5. Jamais de commit direct sur `main` ou `develop`

### Commandes types

```bash
# Début de sprint
git checkout develop
git pull
git checkout -b feature/V0-S1

# Après avoir fini une US
git add .
git commit -m "feat(US-001): environnement de développement"
git push origin feature/V0-S1

# Fin de journée (même si US pas finie)
git add .
git commit -m "chore(US-007): avancement CRUD utilisateurs [WIP]"
git push origin feature/V0-S1
```

---

## Phases de livraison

| Version | Période | Sprints | Focus | SP |
|---------|---------|---------|-------|----|
| **V0** | 1-19 juin | V0-S1, V0-S2, V0-S3 | API pure (tous les endpoints) | ~75 |
| **V0.1** | 22 juin-3 juillet | V01-S1, V01-S2 | Angular Web (dashboard + portail) | ~45 |
| **V1** | 6-17 juillet | V1-S1, V1-S2 | Flutter Mobile + notifications | ~50 |
| **Total** | **7 semaines** | **7 sprints** | | **~170 SP** |

---

## V0 — API Pure (1-19 juin)

Tous les endpoints backend, zéro frontend. L'API est testable via Swagger/Postman.

### Sprint V0-S1 (1-6 juin) — Auth & Core Infrastructure

**Statut :** 🟡 IN_PROGRESS
**Git :** `git checkout -b feature/V0-S1`

---

### EPIC 1 — Infrastructure & DevOps

#### US-001 — Environnement de développement

**Priorité :** 🔴 CRITIQUE | **SP :** 2 | **Statut :** 🟡 IN_PROGRESS

**En tant que** développeur,
**Je veux** disposer d'un environnement de développement fonctionnel,
**Afin de** démarrer le développement dans des conditions reproductibles.

**Critères d'acceptation :**
- Docker Compose ou alternatives locales démarrent Redis et Keycloak
- PostgreSQL via Supabase Cloud
- `.env` externalisé, `.gitignore` configuré
- Spring Boot répond sur `localhost:8081` (health OK)

**Tâches :**
- [API] Configurer `application.yml` (profils dev/prod)
- [API] Créer `.env.example`
- [API] Mettre à jour `.gitignore`

**Git :** `git add . && git commit -m "feat(US-001): environnement de développement" && git push origin feature/V0-S1`

---

#### US-002 — Pipeline CI/CD GitHub Actions

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🟡 IN_PROGRESS

**En tant que** développeur,
**Je veux** un pipeline CI/CD automatisé,
**Afin de** garantir la qualité du code à chaque commit.

**Critères d'acceptation :**
- Pipeline sur push `main` et `develop` : lint → tests → build JAR
- Échec si tests ne passent pas
- GitHub Secrets configurés

**Tâches :**
- [API] Mettre à jour `backend.yml` avec JaCoCo (seuil ≥ 70%)
- [API] Configurer les secrets GitHub

**Git :** `git add . && git commit -m "feat(US-002): pipeline CI/CD GitHub Actions" && git push origin feature/V0-S1`

---

#### US-003 — Migrations Flyway

**Priorité :** 🔴 CRITIQUE | **SP :** 2 | **Statut :** 🔴 PENDING

**En tant que** développeur,
**Je veux** des migrations de schéma versionnées avec Flyway,
**Afin de** garantir la cohérence entre environnements.

**Critères d'acceptation :**
- Flyway configuré, migrations auto au démarrage
- `V1__init.sql` : toutes les tables
- `V2__add_indexes.sql` : index de performance
- Checksum bloquant si migration altérée

**Tâches :**
- [API] Ajouter dépendance Flyway dans `pom.xml`
- [API] Créer `V1__init.sql` et `V2__add_indexes.sql`

**Git :** `git add . && git commit -m "feat(US-003): migrations Flyway" && git push origin feature/V0-S1`

---

### EPIC 2 — Authentification & Sécurité

#### US-004 — Configuration Realm Keycloak

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🟡 IN_PROGRESS

**En tant qu'** administrateur,
**Je veux** le Realm `ng-fields` configuré dans Keycloak,
**Afin de** centraliser l'authentification.

**Critères d'acceptation :**
- Realm exporté dans `infra/keycloak/realm-export.json`
- 3 clients OIDC : backend (confidentiel), mobile (public), web (public)
- 4 rôles : ADMIN, MANAGER, TECHNICIAN, CLIENT_PORTAL
- PKCE pour clients publics, politique mot de passe, Brute Force Detection

**Tâches :**
- [API] Finaliser la configuration (MFA, politique mdp)
- [API] Exporter et versionner le realm JSON

**Git :** `git add . && git commit -m "feat(US-004): configuration Realm Keycloak" && git push origin feature/V0-S1`

---

#### US-005 — Spring Security Resource Server

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🟢 COMPLETED

**En tant que** développeur backend,
**Je veux** que Spring Boot valide les tokens JWT Keycloak,
**Afin de** sécuriser tous les endpoints.

**Critères d'acceptation :**
- Spring Security Resource Server OIDC
- Rôles Keycloak mappés en ROLE_ Spring Security
- CORS configuré, `/actuator/health` public
- Requête sans token → 401

**Tâches :**
- [API] Vérifier `SecurityConfig.java` et `KeycloakRoleConverter.java`
- [API] Tester avec tokens réels (4 rôles)

**Git :** `git add . && git commit -m "feat(US-005): Spring Security Resource Server" && git push origin feature/V0-S1`

---

#### US-006 — Audit Trail

**Priorité :** 🟠 HAUTE | **SP :** 2 | **Statut :** 🟢 COMPLETED

**En tant qu'** administrateur,
**Je veux** un journal d'audit des actions sensibles,
**Afin de** garantir la traçabilité.

**Critères d'acceptation :**
- `AuditService` injectable, table `audit_logs` INSERT-only
- Actions : CRUD Users, Clients, Interventions
- Contient : user_id, action, resource, resource_id, details (JSONB)

**Tâches :**
- [API] Créer `AuditLog` entity + `AuditLogRepository`
- [API] Créer `AuditService`
- [API] Injecter dans UserService, ClientService, InterventionService

**Git :** `git add . && git commit -m "feat(US-006): audit trail" && git push origin feature/V0-S1`

---

### EPIC 3 — Gestion des Utilisateurs

#### US-007 — CRUD Utilisateurs

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🟡 IN_PROGRESS

**En tant qu'** administrateur,
**Je veux** gérer les comptes utilisateurs,
**Afin de** contrôler l'accès à l'application.

**Critères d'acceptation :**
- `POST /api/admin/users` → crée en BDD + Keycloak → 201
- `GET /api/admin/users` → liste paginée
- `GET /api/admin/users/{id}` → détail
- `PUT /api/admin/users/{id}` → modifie
- `DELETE /api/admin/users/{id}` → soft delete
- Réservé ADMIN, journalisé dans audit_logs

**Tâches :**
- [API] Créer `UserRepository` (manquant)
- [API] Vérifier `UserController` + `UserService` existants
- [API] Implémenter Keycloak Admin API synchro
- [API] DTOs et validation
- [API] Tests unitaires + intégration

**Git :** `git add . && git commit -m "feat(US-007): CRUD utilisateurs" && git push origin feature/V0-S1`

---

#### US-008 — Profil utilisateur (self-service)

**Priorité :** 🟡 MOYENNE | **SP :** 1 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** consulter et modifier mon profil,
**Afin de** maintenir mes informations à jour.

**Critères d'acceptation :**
- `GET /api/users/me` → profil connecté
- `PUT /api/users/me` → modifie nom, téléphone (pas rôle ni email)

**Tâches :**
- [API] Endpoints `/me` dans UserController
- [API] SecurityUtils helper

**Git :** `git add . && git commit -m "feat(US-008): profil utilisateur" && git push origin feature/V0-S1`

---

### Sprint V0-S2 (8-12 juin) — Clients & Interventions API

**Statut :** 🔴 PENDING
**Git :** `git checkout -b feature/V0-S2`

---

### EPIC 4 — Gestion des Clients

#### US-009 — CRUD Clients

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🟢 COMPLETED

**En tant qu'** administrateur,
**Je veux** gérer les fiches clients,
**Afin de** maintenir un référentiel centralisé.

**Critères d'acceptation :**
- CRUD complet : POST, GET, GET/{id}, PUT, DELETE (soft)
- `GET /api/clients/{id}/interventions` → historique
- Pagination, filtres, validation
- Réservé ADMIN (création/modif), tous (lecture)

**Tâches :**
- [API] Vérifier et compléter `ClientController` + `ClientService`
- [API] Validation Bean Validation
- [API] Tests

**Git :** `git add . && git commit -m "feat(US-009): CRUD clients" && git push origin feature/V0-S2`

---

#### US-010 — Recherche clients

**Priorité :** 🟡 MOYENNE | **SP :** 2 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** rechercher un client par nom, email, téléphone,
**Afin de** retrouver rapidement une fiche.

**Critères d'acceptation :**
- `GET /api/clients/search?q={terme}` → ILIKE multi-champs
- Max 20 résultats, < 2s pour 1000 clients

**Tâches :**
- [API] Implémenter la recherche dans `ClientRepository`
- [API] Index PostgreSQL

**Git :** `git add . && git commit -m "feat(US-010): recherche clients" && git push origin feature/V0-S2`

---

### EPIC 5 — Module Interventions

#### US-011 — Création d'une intervention

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🟢 COMPLETED

**En tant que** technicien,
**Je veux** créer une fiche d'intervention,
**Afin de** démarrer le suivi numérique.

**Critères d'acceptation :**
- `POST /api/interventions` → 201, statut PENDING
- Associée au technicien JWT
- `local_id` optionnel pour déduplication offline
- Idempotence sur `local_id`

**Tâches :**
- [API] Vérifier `InterventionService.createIntervention()`
- [API] Tester l'idempotence

**Git :** `git add . && git commit -m "feat(US-011): création intervention" && git push origin feature/V0-S2`

---

#### US-012 — Sections 1-2 : Infos générales & Horaires

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** saisir les informations et horaires,
**Afin de** documenter la prestation.

**Critères d'acceptation :**
- `PATCH /api/interventions/{id}` : horaires, calcul auto durée
- Validation cohérence temporelle
- Ownership : technicien propriétaire ou MANAGER/ADMIN

**Tâches :**
- [API] Implementer `updateIntervention()` avec calcul durée
- [API] Validation d'ownership

**Git :** `git add . && git commit -m "feat(US-012): sections infos et horaires" && git push origin feature/V0-S2`

---

#### US-013 — Sections 3-4 : Diagnostic, Travaux & Consommables

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** saisir le diagnostic, les travaux et les pièces,
**Afin de** constituer le corps technique du rapport.

**Critères d'acceptation :**
- `PATCH /api/interventions/{id}` : problem_desc, diagnosis, work_done, équipement
- CRUD items : POST, GET, PUT, DELETE `/api/interventions/{id}/items`
- Champs texte max 5000 caractères

**Tâches :**
- [API] Ajouter endpoints items dans `InterventionController`
- [API] Créer `InterventionItemService`

**Git :** `git add . && git commit -m "feat(US-013): diagnostic et consommables" && git push origin feature/V0-S2`

---

#### US-014 — Gestion pièces et consommables

**Priorité :** 🟠 HAUTE | **SP :** 2 | **Statut :** 🟢 COMPLETED

**En tant que** technicien,
**Je veux** gérer les pièces et consommables,
**Afin de** constituer la base facturable.

**Critères d'acceptation :**
- POST/GET/PUT/DELETE items
- name (obligatoire), quantity (min 1)
- Suppression réservée au propriétaire ou ADMIN/MANAGER

**Tâches :**
- [API] Vérifier `InterventionItem` entity + repository existants
- [API] Compléter les endpoints si nécessaire

**Git :** `git add . && git commit -m "feat(US-014): gestion pièces et consommables" && git push origin feature/V0-S2`

---

#### US-017 — Résultat, recommandations, facturation

**Priorité :** 🔴 CRITIQUE | **SP :** 2 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** indiquer le résultat et les recommandations,
**Afin de** clôturer la fiche.

**Critères d'acceptation :**
- `PATCH` : result (RESOLVED/PARTIAL/UNRESOLVED), recommendations, billable
- result obligatoire pour passer à COMPLETED
- Si UNRESOLVED → follow_up_recommended: true

**Tâches :**
- [API] Étendre `UpdateInterventionRequest`
- [API] Logique `follow_up_recommended`

**Git :** `git add . && git commit -m "feat(US-017): résultat et recommandations" && git push origin feature/V0-S2`

---

#### US-018 — Consultation et liste

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🟡 IN_PROGRESS

**En tant que** manager ou technicien,
**Je veux** consulter la liste des interventions avec filtres,
**Afin de** piloter l'activité.

**Critères d'acceptation :**
- GET paginé avec filtres (status, technician, client, date)
- Technicien → ses interventions ; MANAGER/ADMIN → tout
- `GET /api/interventions/{id}` détail complet
- `GET /api/interventions/stats` KPIs

**Tâches :**
- [API] Vérifier endpoints existants dans `InterventionController`
- [API] Implémenter `/stats`
- [API] Index PostgreSQL

**Git :** `git add . && git commit -m "feat(US-018): consultation et liste" && git push origin feature/V0-S2`

---

### Sprint V0-S3 (15-19 juin) — Media, Sync & Envoi API

**Statut :** 🟡 IN_PROGRESS (portage monolithe → microservices terminé le 02/07)
**Git :** `git checkout -b feature/V0-S3`

---

#### US-015 — Photos avant/après

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🟢 COMPLETED

**En tant que** technicien,
**Je veux** uploader des photos avant/après,
**Afin de** constituer les preuves visuelles.

**Critères d'acceptation :**
- `POST /api/interventions/{id}/photos` (multipart, type BEFORE/AFTER)
- Limite 5/catégorie → 422
- Stockage Supabase Storage, URLs signées 72h
- DELETE supprime storage + BDD
- Taille max 10 Mo

**Tâches :**
- [API] Vérifier `PhotoController` + `PhotoService` + `InterventionPhoto`
- [API] Adapter `StorageService` pour Supabase Storage

**Git :** `git add . && git commit -m "feat(US-015): photos avant/après" && git push origin feature/V0-S3`

---

#### US-016 — Signatures électroniques

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🟢 COMPLETED

**En tant que** technicien,
**Je veux** recueillir 3 signatures (client, tech, responsable),
**Afin de** valider juridiquement la fiche.

**Critères d'acceptation :**
- 3 endpoints : /client, /technician, /manager (PNG base64)
- Manager : réservé MANAGER/ADMIN
- signature_refused + refusal_reason
- Client + tech → statut COMPLETED
- URLs signées 72h

**Tâches :**
- [API] Vérifier `SignatureController` + `SignatureService`
- [API] Transition COMPLETED automatique

**Git :** `git add . && git commit -m "feat(US-016): signatures électroniques" && git push origin feature/V0-S3`

---

#### US-020 — Géolocalisation GPS

**Priorité :** 🟠 HAUTE | **SP :** 2 | **Statut :** 🔴 PENDING

**En tant que** manager,
**Je veux** la position d'arrivée enregistrée,
**Afin de** disposer d'une preuve géolocalisée.

**Critères d'acceptation :**
- Champs gps_latitude/gps_longitude (migration Flyway V3)
- `PATCH` accepte les coordonnées
- Validation : [-90,90], [-180,180]

**Tâches :**
- [API] Migration Flyway V3
- [API] Validation dans le DTO

**Git :** `git add . && git commit -m "feat(US-020): géolocalisation GPS" && git push origin feature/V0-S3`

---

#### US-021 — Rapport PDF automatique

**Priorité :** 🔴 CRITIQUE | **SP :** 8 | **Statut :** 🟢 COMPLETED

**En tant que** technicien,
**Je veux** un rapport PDF généré automatiquement,
**Afin de** disposer d'un document officiel.

**Critères d'acceptation :**
- Déclenché à COMPLETED ou manuellement (POST /pdf)
- Contient : logo, 8 sections, photos, signatures, QR code
- Stocké Supabase Storage, URL signée 72h
- Génération asynchrone (Redis queue)
- Max 3 retries

**Tâches :**
- [API] Vérifier `PdfService` (OpenPDF + ZXing)
- [API] Créer `PdfQueueConsumer`
- [API] Configurer retry

**Git :** `git add . && git commit -m "feat(US-021): rapport PDF automatique" && git push origin feature/V0-S3`

---

#### US-022 — Envoi du rapport par Email

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** envoyer le PDF par email,
**Afin de** transmettre la preuve au client.

**Critères d'acceptation :**
- `POST /api/interventions/{id}/send/email`
- Destinataires par défaut : email du client
- Queue Redis email_queue, retry backoff max 24h
- Journalisé dans audit_logs

**Tâches :**
- [API] Créer `EmailService` (JavaMailSender)
- [API] Créer `EmailQueueConsumer`
- [API] Tester avec GreenMail

**Git :** `git add . && git commit -m "feat(US-022): envoi rapport par email" && git push origin feature/V0-S3`

---

#### US-023 — Envoi du rapport via WhatsApp

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** envoyer le PDF par WhatsApp,
**Afin de** partager sur le canal privilégié.

**Critères d'acceptation :**
- `POST /api/interventions/{id}/send/whatsapp`
- Numéro par défaut : téléphone client
- Fallback email si API WhatsApp indisponible
- Queue Redis whatsapp_queue

**Tâches :**
- [API] Créer `WhatsAppService` (API Meta Cloud)
- [API] Créer `WhatsAppQueueConsumer` avec fallback
- [API] Tester avec WireMock

**Git :** `git add . && git commit -m "feat(US-023): envoi rapport WhatsApp" && git push origin feature/V0-S3`

---

#### US-019 — Synchronisation hors-ligne

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🔴 PENDING

**En tant que** technicien,
**Je veux** synchroniser les fiches créées hors-ligne,
**Afin de** garantir la continuité sans réseau.

**Critères d'acceptation :**
- `POST /api/sync/interventions` → batch avec local_id
- Last-write-wins, erreur partielle gérée
- Idempotent, batch 10 fiches < 60s

**Tâches :**
- [API] Créer `SyncController` + `SyncService`
- [API] Implémenter last-write-wins
- [API] Tests idempotence + performance

**Git :** `git add . && git commit -m "feat(US-019): synchronisation hors-ligne" && git push origin feature/V0-S3`

---

### US-036 — Optimisation API & Clean Architecture
Priorité : 🟡 MOYENNE | SP : 3 | Statut : 🔴 PENDING
En tant que développeur,
Je veux optimiser les payloads API et respecter la Clean Architecture,
Afin d'améliorer les performances du dashboard et la maintenabilité.
Critères d'acceptation :
[API] Utilisation de DTOs de projection (Records Java) pour les listes (réduction du payload JSON)
[API] Zéro logique métier dans les Controllers (validation et délégation uniquement)
[API] Appels OpenProject/WhatsApp 100% asynchrones via files Redis
Tâches :
[API] Créer les Records `InterventionListDTO`, `ClientListDTO`
[API] Auditer et nettoyer les Controllers
[API] Vérifier l'asynchronisme strict des services d'intégration
Git : `git add . && git commit -m "refactor(US-036): optimisation API et clean architecture" && git push origin feature/V0-S3`

---

## V0.1 — Angular Web (22 juin-3 juillet)

Tous les écrans web Angular, connectés à l'API V0.

### Sprint V01-S1 (22-26 juin) — Dashboard Angular

**Statut :** 🟡 IN_PROGRESS (scaffolding fait, features métier à implémenter)
**Git :** `git checkout develop && git pull && git checkout -b feature/V01-S1`

---

### EPIC 9 — Tableau de Bord Web

### US-028 — Dashboard Manager (API + Angular)
Priorité : 🔴 CRITIQUE | SP : 10 (était 8) | Statut : 🔴 PENDING
En tant que manager,
Je veux un tableau de bord web avec statistiques et vue Kanban,
Afin de piloter l'activité en temps réel avec une UX moderne.
Critères d'acceptation :
`GET /api/manager/dashboard` : KPIs du jour
`GET /api/manager/stats/by-technician` : stats par technicien
`GET /api/manager/stats/by-client` : stats par client
`GET /api/manager/export/csv` : export CSV
`GET /api/manager/export/excel` : export XLSX
Réservé MANAGER/ADMIN, < 3s
[WEB] Dashboard Angular avec graphiques
[WEB] Liste interventions avec filtres
[WEB] Export CSV/Excel
**[WEB] Vue Kanban / Timeline en complément de l'AG Grid**
**[WEB] Skeleton Loaders pour les KPIs et listes (perceived performance)**
**[WEB] Mode Sombre (Dark Mode) via Tailwind CSS**
Tâches :
[API] `DashboardController` + `DashboardService`
[API] `ExportService` (Apache POI + OpenCSV)
[WEB] Initialiser projet Angular (`apps/web/`)
[WEB] Module dashboard (graphiques Chart.js)
[WEB] Liste interventions avec filtres
[WEB] Détail intervention
[WEB] Boutons export
**[WEB] Implémenter la vue Kanban/Timeline (ex: Angular CDK Drag & Drop)**
**[WEB] Intégrer les Skeleton Loaders (Tailwind / ngx-skeleton-loader)**
**[WEB] Configurer le Dark Mode (Tailwind `darkMode: 'class'`)**
Git : `git add . && git commit -m "feat(US-028): dashboard manager" && git push origin feature/V01-S1`

---

### US-035 — Temps réel Dashboard (SSE)
Priorité : 🟠 HAUTE | SP : 5 | Statut : 🔴 PENDING
En tant que manager,
Je veux que le dashboard se mette à jour sans refresh de la page,
Afin de suivre l'activité en temps réel (effet "waouh").
Critères d'acceptation :
[API] Endpoint SSE `/api/manager/events`
[API] Push des événements `INTERVENTION_STATUS_CHANGED`, `NEW_TICKET`
[WEB] Consommation du flux SSE et rafraîchissement automatique de l'AG Grid / Kanban
Tâches :
[API] Configurer `SseEmitter` dans un `EventController`
[API] Émettre les événements depuis `InterventionService` et `ClientPortalService`
[WEB] Créer un `EventSourceService` dans Angular pour consommer le SSE
Git : `git add . && git commit -m "feat(US-035): temps réel dashboard SSE" && git push origin feature/V01-S1`

---

#### US-029 — Planning techniciens (API + Angular)

**Priorité :** 🟠 HAUTE | **SP :** 5 | **Statut :** 🔴 PENDING

**En tant que** manager,
**Je veux** planifier et affecter des interventions,
**Afin d'** organiser les tournées.

**Critères d'acceptation :**
- `POST /api/manager/interventions` → crée planifiée
- `PUT /api/manager/interventions/{id}/assign` → réaffecte
- `GET /api/manager/technicians/{id}/schedule` → planning
- DELETE → annule (CANCELLED)
- [WEB] Vue planning hebdomadaire
- [WEB] Sélecteur technicien

**Tâches :**
- [API] Endpoints manager dans `InterventionService`
- [API] Validation transitions statut
- [WEB] Composant planning

**Git :** `git add . && git commit -m "feat(US-029): planning techniciens" && git push origin feature/V01-S1`

---

#### US-007W — CRUD Users (Écrans Angular)

**Priorité :** 🟠 HAUTE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant qu'** administrateur,
**Je veux** gérer les utilisateurs depuis le web,
**Afin de** administrer l'application.

**Critères d'acceptation :**
- [WEB] Liste utilisateurs paginée
- [WEB] Formulaire création/édition
- [WEB] Désactivation d'un compte

**Tâches :**
- [WEB] `UserListComponent`
- [WEB] `UserFormComponent`

**Git :** `git add . && git commit -m "feat(US-007W): écrans utilisateurs Angular" && git push origin feature/V01-S1`

---

#### US-009W — CRUD Clients (Écrans Angular)

**Priorité :** 🟠 HAUTE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant que** administrateur,
**Je veux** gérer les clients depuis le web,
**Afin de** maintenir le référentiel.

**Critères d'acceptation :**
- [WEB] Liste clients avec recherche
- [WEB] Création/édition client
- [WEB] Détail client + historique interventions

**Tâches :**
- [WEB] `ClientListComponent`
- [WEB] `ClientFormComponent`
- [WEB] `ClientDetailComponent`

**Git :** `git add . && git commit -m "feat(US-009W): écrans clients Angular" && git push origin feature/V01-S1`

---

#### US-011W → 018W — Interventions (Écrans Angular)

**Priorité :** 🟠 HAUTE | **SP :** 5 | **Statut :** 🔴 PENDING

**En tant que** manager,
**Je veux** voir et gérer les interventions depuis le web,
**Afin de** superviser l'activité.

**Critères d'acceptation :**
- [WEB] Liste avec tous les filtres
- [WEB] Détail complet (8 sections, photos, signatures)
- [WEB] Déclencher PDF, Email, WhatsApp
- [WEB] Voir les stats

**Tâches :**
- [WEB] `InterventionListComponent`
- [WEB] `InterventionDetailComponent`
- [WEB] Boutons d'action

**Git :** `git add . && git commit -m "feat(US-011W): écrans interventions Angular" && git push origin feature/V01-S1`

---

### Sprint V01-S2 (29 juin-3 juillet) — Portail & Intégrations

**Statut :** 🟡 IN_PROGRESS (en cours)
**Git :** `git checkout develop && git pull && git checkout -b feature/V01-S2`

---

### EPIC 10 — Portail Client & OpenProject

#### US-026 — Portail client (Angular)

**Priorité :** 🟠 HAUTE | **SP :** 8 | **Statut :** 🔴 PENDING

**En tant que** client,
**Je veux** soumettre une demande d'intervention,
**Afin de** signaler un problème rapidement.

**Critères d'acceptation :**
- `POST /api/client-portal/requests` public (rate limit 5/IP/h)
- Champs : nom, email, phone, issue_type, description, urgency
- Crée ticket OpenProject automatiquement
- Email de confirmation au client
- Queue Redis si OP indisponible
- [WEB] Page portail public
- [WEB] Page confirmation avec numéro de ticket

**Tâches :**
- [API] `ClientPortalController` + `ClientPortalService`
- [API] Rate limiting
- [WEB] Page portail (Angular)
- [WEB] Page confirmation

**Git :** `git add . && git commit -m "feat(US-026): portail client" && git push origin feature/V01-S2`

---

#### US-027 — Intégration OpenProject API v3

**Priorité :** 🟠 HAUTE | **SP :** 5 | **Statut :** 🔴 PENDING

**En tant que** système NG-Fields,
**Je veux** créer et suivre des tickets OpenProject,
**Afin de** centraliser le suivi des demandes.

**Critères d'acceptation :**
- `createTicket()` → retourne ID
- `updateTicket()` → met à jour statut
- `getTicket()` → consulte
- Bearer Token, queue Redis, retry max 24h
- Échecs dans audit_logs

**Tâches :**
- [API] `OpenProjectClient` (WebClient)
- [API] `OpenProjectService`
- [API] `OpenProjectQueueConsumer`
- [API] Tests WireMock

**Git :** `git add . && git commit -m "feat(US-027): intégration OpenProject" && git push origin feature/V01-S2`

---

### EPIC 11 — Monitoring

#### US-030 — Prometheus / Actuator

**Priorité :** 🟡 MOYENNE | **SP :** 3 | **Statut :** 🔴 PENDING

**En tant qu'** administrateur,
**Je veux** superviser la santé du système,
**Afin de** détecter les anomalies.

**Critères d'acceptation :**
- `/actuator/health` : status DB, Redis, Keycloak, Storage
- `/actuator/prometheus` : métriques OpenMetrics
- Métriques custom
- Logs JSON structurés

**Tâches :**
- [API] Configurer Actuator + Micrometer
- [API] Métriques custom
- [API] Logback JSON

**Git :** `git add . && git commit -m "feat(US-030): monitoring Prometheus" && git push origin feature/V01-S2`

---

## V1 — Flutter Mobile (6-17 juillet)

Tous les écrans Flutter, mode hors-ligne, notifications.

### Sprint V1-S1 (6-10 juillet) — Mobile Core

**Statut :** 🔴 PENDING
**Git :** `git checkout develop && git pull && git checkout -b feature/V1-S1`

---

### EPIC 3 (suite) — Utilisateurs Mobile

#### US-007M — CRUD Utilisateurs (Mobile)

**Priorité :** 🟠 HAUTE | **SP :** 3 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Écran liste utilisateurs (Admin)
- [MOBILE] Écran création/édition

**Tâches :**
- [MOBILE] `UserListScreen`
- [MOBILE] `UserFormScreen`

**Git :** `git add . && git commit -m "feat(US-007M): écrans utilisateurs mobile" && git push origin feature/V1-S1`

---

#### US-008M — Profil (Mobile)

**Priorité :** 🟡 MOYENNE | **SP :** 1 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Écran profil avec affichage et édition

**Tâches :**
- [MOBILE] `ProfileScreen`

**Git :** `git add . && git commit -m "feat(US-008M): profil mobile" && git push origin feature/V1-S1`

---

### EPIC 4 (suite) — Clients Mobile

#### US-009M — CRUD Clients (Mobile)

**Priorité :** 🟠 HAUTE | **SP :** 3 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Liste avec recherche
- [MOBILE] Création/édition
- [MOBILE] Détail + historique

**Tâches :**
- [MOBILE] `ClientListScreen`
- [MOBILE] `ClientFormScreen`
- [MOBILE] `ClientDetailScreen`

**Git :** `git add . && git commit -m "feat(US-009M): écrans clients mobile" && git push origin feature/V1-S1`

---

### EPIC 5 (suite) — Interventions Mobile

#### US-011M — Création intervention (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Formulaire création : client, type, date
- [MOBILE] Appel API avec Dio

**Tâches :**
- [MOBILE] `InterventionCreateScreen`
- [MOBILE] Intégration API

**Git :** `git add . && git commit -m "feat(US-011M): création intervention mobile" && git push origin feature/V1-S1`

---

#### US-012M — Sections 1-2 (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Infos générales + horaires
- [MOBILE] Date/time picker, durée calculée

**Tâches :**
- [MOBILE] `InterventionFormStep1` (infos)
- [MOBILE] `InterventionFormStep2` (horaires)

**Git :** `git add . && git commit -m "feat(US-012M): sections infos et horaires mobile" && git push origin feature/V1-S1`

---

### US-013M — Sections 3-4 (Mobile)
Priorité : 🔴 CRITIQUE | SP : 5 (était 3) | Statut : 🔴 PENDING
En tant que technicien,
Je veux saisir le diagnostic et les travaux via la voix et avec une UI adaptée au terrain,
Afin de réduire le temps de saisie et d'utiliser l'app avec des gants/en extérieur.
Critères d'acceptation :
[MOBILE] Diagnostic, travaux, équipement
[MOBILE] Liste consommables avec ajout/suppression
**[MOBILE] Bouton "Microphone" pour saisie vocale (Voice-to-Text) du diagnostic**
**[MOBILE] UI "Terrain" : gros boutons, contrastes forts (Material 3, police min 16sp)**
Tâches :
[MOBILE] `InterventionFormStep3` (diagnostic)
[MOBILE] `InterventionFormStep4` (consommables)
**[MOBILE] Intégrer le package `speech_to_text` pour la dictée**
**[MOBILE] Adapter le thème global (visualDensity, tailles de police) pour l'ergonomie terrain**
Git : `git add . && git commit -m "feat(US-013M): diagnostic et consommables mobile" && git push origin feature/V1-S1`

---

#### US-018M — Dashboard & Liste (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Dashboard tech avec interventions du jour
- [MOBILE] Détail intervention complet

**Tâches :**
- [MOBILE] `DashboardScreen`
- [MOBILE] `InterventionDetailScreen`

**Git :** `git add . && git commit -m "feat(US-018M): dashboard et liste mobile" && git push origin feature/V1-S1`

---

### Sprint V1-S2 (13-17 juillet) — Mobile Advanced

**Statut :** 🔴 PENDING
**Git :** `git checkout develop && git pull && git checkout -b feature/V1-S2`

---

#### US-015M — Photos (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Capture photo (avant/après)
- [MOBILE] Compression avant upload
- [MOBILE] Galerie avec compteur 5/5

**Tâches :**
- [MOBILE] `PhotoCaptureScreen`
- [MOBILE] Compression et upload

**Git :** `git add . && git commit -m "feat(US-015M): photos mobile" && git push origin feature/V1-S2`

---

#### US-016M — Signatures (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 3 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Canvas signature tactile (3 zones)
- [MOBILE] Option refus avec motif

**Tâches :**
- [MOBILE] `SignatureScreen`
- [MOBILE] Canvas + envoi PNG base64

**Git :** `git add . && git commit -m "feat(US-016M): signatures mobile" && git push origin feature/V1-S2`

---

#### US-017M — Résultat (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 2 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Sections résultat, recommandations, facturation

**Tâches :**
- [MOBILE] `InterventionFormStep6` (résultat)
- [MOBILE] `InterventionFormStep7` (facturation)

**Git :** `git add . && git commit -m "feat(US-017M): résultat mobile" && git push origin feature/V1-S2`

---

#### US-019M — Synchronisation hors-ligne (Mobile)

**Priorité :** 🔴 CRITIQUE | **SP :** 8 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Base locale Drift (SQLite) avec les mêmes tables
- [MOBILE] Queue de sync automatique au retour réseau
- [MOBILE] OfflineBadge indicateur de connexion
- **[MOBILE] Badge de sync rassurant : animation de sync en arrière-plan + compteur de fiches en attente**
- [MOBILE] Sync background avec connectivity_plus

**Tâches :**
- [MOBILE] Configurer Drift (entités, migrations)
- [MOBILE] `SyncService` (queue + retry)
- [MOBILE] `OfflineBadge` widget
- **[MOBILE] Développer le widget `OfflineBadge` avec animation et compteur**

**Git :** `git add . && git commit -m "feat(US-019M): synchronisation hors-ligne mobile" && git push origin feature/V1-S2`

---

#### US-020M — GPS (Mobile)

**Priorité :** 🟠 HAUTE | **SP :** 2 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Capture GPS auto à l'arrivée
- [MOBILE] Intégration geolocator

**Tâches :**
- [MOBILE] Géolocalisation automatique

**Git :** `git add . && git commit -m "feat(US-020M): GPS mobile" && git push origin feature/V1-S2`

---

#### US-021M — PDF (Mobile)

**Priorité :** 🟠 HAUTE | **SP :** 1 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Bouton télécharger le PDF
- [MOBILE] Aperçu et partage natif

**Tâches :**
- [MOBILE] Téléchargement + partage (share_plus)

**Git :** `git add . && git commit -m "feat(US-021M): PDF mobile" && git push origin feature/V1-S2`

---

#### US-022M — Email (Mobile)

**Priorité :** 🟠 HAUTE | **SP :** 1 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Bouton envoyer par email
- [MOBILE] Sélection destinataires

**Tâches :**
- [MOBILE] `EmailSendScreen`

**Git :** `git add . && git commit -m "feat(US-022M): email mobile" && git push origin feature/V1-S2`

---

#### US-023M — WhatsApp (Mobile)

**Priorité :** 🟠 HAUTE | **SP :** 1 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- [MOBILE] Bouton envoyer par WhatsApp

**Tâches :**
- [MOBILE] WhatsApp button + deep link

**Git :** `git add . && git commit -m "feat(US-023M): WhatsApp mobile" && git push origin feature/V1-S2`

---

### EPIC 11 (suite) — Notifications Mobile

#### US-024 — Notifications push Firebase

**Priorité :** 🟠 HAUTE | **SP :** 6 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- Push managers sur : création, COMPLETED, dépassement seuil
- `POST /api/users/me/push-token` enregistre token FCM
- Queue Redis push_queue, fallback email
- [MOBILE] Réception notification + navigation directe
- [WEB] Page configuration alertes
- **[API] Tâches planifiées (`@Scheduled`) pour détecter les dépassements de seuil de durée (SLA)**

**Tâches :**
- [API] `NotificationService` + `PushQueueConsumer`
- [API] Endpoint push-token + settings
- [MOBILE] Configurer Firebase Cloud Messaging
- [MOBILE] Gérer réception + navigation
- [WEB] Page configuration seuil durée
- **[API] Créer un job `@Scheduled` pour scanner les interventions `IN_PROGRESS` et alerter (push_queue)**

**Git :** `git add . && git commit -m "feat(US-024): notifications push Firebase" && git push origin feature/V1-S2`

---

#### US-025 — Notification nouveau ticket

**Priorité :** 🟡 MOYENNE | **SP :** 2 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- Soumission portail → push + email aux managers
- < 1 minute

**Tâches :**
- [API] Injecter `NotificationService` dans `ClientPortalService`

**Git :** `git add . && git commit -m "feat(US-025): notification nouveau ticket" && git push origin feature/V1-S2`

---

### EPIC 12 — Tests & Déploiement

#### US-031 — Tests E2E et UAT

**Priorité :** 🔴 CRITIQUE | **SP :** 8 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- Test E2E : auth → client → intervention → photos → signatures → PDF → email
- Test offline : batch 5 fiches → sync
- Test portail : soumission → OP → notification
- Test charge : 20 utilisateurs, P95 < 2s
- Couverture ≥ 70%
- Aucun bug critique

**Tâches :**
- [API] Tests E2E Testcontainers
- [API] Tests charge k6
- [API] Rapport JaCoCo
- [MOBILE] Tests intégration Flutter
- [WEB] Tests composants Angular

**Git :** `git add . && git commit -m "feat(US-031): tests E2E et UAT" && git push origin feature/V1-S2`

---

#### US-032 — Déploiement production

**Priorité :** 🔴 CRITIQUE | **SP :** 5 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- Déploiement VPS ou cloud
- TLS Let's Encrypt
- Domaines configurés
- Sauvegardes auto
- Smoke test post-déploiement
- Builds stores : APK + TestFlight

**Tâches :**
- [API] Déployer API + Keycloak
- [API] Nginx + TLS
- [API] Migrations Flyway prod
- [MOBILE] APK Play Store
- [MOBILE] TestFlight iOS

**Git :** `git add . && git commit -m "feat(US-032): déploiement production" && git push origin feature/V1-S2`

---

#### US-033 — Documentation API Swagger

**Priorité :** 🟠 HAUTE | **SP :** 2 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- Swagger UI complet, tous les endpoints documentés
- Schéma Bearer Token

**Tâches :**
- [API] Annotations @Operation, @ApiResponse sur tous les controllers

**Git :** `git add . && git commit -m "feat(US-033): documentation API Swagger" && git push origin feature/V1-S2`

---

#### US-034 — Documentation projet

**Priorité :** 🟡 MOYENNE | **SP :** 2 | **Statut :** 🔴 PENDING

**Critères d'acceptation :**
- Guide installation, guide utilisateur, README à jour

**Tâches :**
- [API] Guide déploiement
- [MOBILE] Guide utilisateur

**Git :** `git add . && git commit -m "feat(US-034): documentation projet" && git push origin feature/V1-S2`

---

## Statut par sprint (02/07/2026)

| Sprint | Dates | Statut | US COMPLETED | US IN PROGRESS | US PENDING |
|--------|-------|--------|--------------|----------------|------------|
| V0-S1 | 1-6 juin | 🟢 COMPLETED | US-001, US-002, US-003, US-004, US-005, US-006, US-007, US-008 | — | — |
| V0-S2 | 8-12 juin | 🟡 IN_PROGRESS | US-009, US-011, US-014 | US-018 | US-010, US-012, US-013, US-017 |
| V0-S3 | 15-19 juin | 🟡 IN_PROGRESS | US-015, US-016, US-021 | — | US-019, US-020, US-022, US-023 |
| V01-S1 | 22-26 juin | 🟡 IN_PROGRESS | — | — | US-028, US-029, US-007W, US-009W, US-011W |
| V01-S2 | 29 juin-3 juillet | 🟡 IN_PROGRESS | — | — | US-026, US-027, US-030 |
| V1-S1 | 6-10 juillet | 🔴 PENDING | — | — | US-007M, US-008M, US-009M, US-011M, US-012M, US-013M, US-018M |
| V1-S2 | 13-17 juillet | 🔴 PENDING | — | — | US-015M, US-016M, US-017M, US-019M, US-020M, US-021M, US-022M, US-023M, US-024, US-025, US-031, US-032, US-033, US-034 |

---

## Récapitulatif par version

| Version | Sprints | US | SP | COMPLETED | IN PROGRESS | Livrables |
|---------|---------|----|----|-----------|-------------|-----------|
| **V0** | 3 (1-19 juin) | 23 API | ~75 | 10 (US-001→008, 015, 016) | 1 (US-018) | Tous les endpoints backend (photos + signatures portés le 02/07) |
| **V0.1** | 2 (22 juin-3 juillet) | 9 Web | ~45 | — | — | Dashboard Angular, portail |
| **V1** | 2 (6-17 juillet) | 17 Mobile | ~50 | — | — | App Flutter complète |
| **Total** | **7 sprints** | **~29 US** | **~170 SP** | **10** | **1** | |

---

## Matrice de dépendances

```
US-001 → 002 → 003 → (toutes les US)
                              │
US-004 → 005 → 007 (Users)  ←┘
           │    └→ 008 (Profil)
           │
           └→ 009 → 010 (Clients)
                    │
                    └→ 011 → 012 → 013 → 014 → 017 → 018
                                                    │
                    ┌────────────────────────────────┘
                    ↓
              015 → 016 → 021 → 022
              020            023
              019

V0.1 : 028 → 029, 026 → 027, 030
V1   : Tous écrans Mobile + 024 → 025 → 031 → 032 → 033/034
```

---

## Risques

| Risque | Probabilité | Impact | Mitigation |
|--------|-------------|--------|------------|
| Complexité OIDC Flutter | Haute | Critique | POC auth dès V1-S1 (6 juillet) |
| Performance PDF avec photos | Moyenne | Haute | Génération asynchrone Redis queue |
| Quota API WhatsApp (Meta) | Moyenne | Haute | Fallback email avant mise en production |
| Délai validation App Store iOS | Haute | Moyenne | Soumettre TestFlight dès V1-S1 |
| Problèmes Docker locaux | Haute | Haute | Solutions alternatives documentées dans Setup.md |
| Planning 7 semaines pour 1 stagiaire | Haute | Haute | Priorité V0 + V0.1 ; V1 peut glisser |

---

## Définition du « DONE » (DoD)

- Le code est commité avec message conventionnel
- Tous les tests unitaires passent (couverture ≥ 70%)
- Les tests d'intégration passent
- Le lint passe sans erreur
- La PR a été revue
- La documentation Swagger est à jour
- Le pipeline CI/CD passe
- La fonctionnalité est démontrable
- Le statut de l'US est marqué COMPLETED

---

_Version 4.0 — 02/07/2026 — Prochaine révision : fin V01-S2 (03/07/2026)_

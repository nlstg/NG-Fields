# Tests API NG-Fields (Postman)

## Prérequis

- **Keycloak** sur `http://localhost:8080` (realm `ng-fields`)
- **API Spring Boot** sur `http://localhost:8081`

## Installation

1. `File` → `Import` → onglet `Raw text`
2. Ouvrir `docs/tests/postman-collection.json`, copier le contenu
3. Coller dans Postman → `Import`

## Requêtes

### Health & Auth (1-6)

| # | Requête | Username | Mot de passe | Code |
|---|---------|----------|-------------|:---:|
| 1 | Health check | — | — | 200 |
| 2 | Login CLIENT_PORTAL | `client1` | `Client123!` | 200 |
| 3 | Login TECHNICIEN | `tech1` | `Tech123!` | 200 |
| 4 | Login MANAGER | `manager1` | `Mngr123!` | 200 |
| 5 | Login ADMIN | `admin` | `Admin123!` | 200 |
| 6 | Identifiants invalides | `user_inexistant` | `mauvais_mdp` | 401 |

### Inscription (7-8)

| # | Requête | Body | Code |
|---|---------|------|:---:|
| 7 | Inscription publique | `inscrit_test` / `InscritPass123!` | 201 |
| 8 | Inscription doublon | Même username | 400 |

### Profil (9-13)

| # | Requête | Token | Code |
|---|---------|-------|:---:|
| 9 | GET /admin/me (CLIENT_PORTAL) | `userToken` | 200 |
| 10 | GET /admin/me (TECHNICIEN) | `technicianToken` | 200 |
| 11 | GET /admin/me (MANAGER) | `managerToken` | 200 |
| 12 | GET /admin/me (ADMIN) | `adminToken` | 200 |
| 13 | GET /admin/me (non auth) | Aucun | 401 |

### Clients (14-20)

| # | Requête | Auth | Code |
|---|---------|------|:---:|
| 14 | Créer un client (ADMIN) | `adminToken` | 201 |
| 15 | Lister les clients | `adminToken` | 200 |
| 16 | Rechercher un client | `adminToken` | 200 |
| 17 | Détail d'un client | `adminToken` | 200 |
| 18 | Modifier un client (ADMIN) | `adminToken` | 200 |
| 19 | Supprimer un client (ADMIN) | `adminToken` | 204 |
| 20 | Créer un client (CLIENT_PORTAL) | `userToken` | 403 |

## Ordre d'exécution

Exécuter dans l'ordre : **1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9 → 10 → 11 → 12 → 13 → 14 → 15 → 16 → 17 → 18 → 19 → 20**

Les requêtes 14 à 20 dépendent de `testClientId` (généré automatiquement par la 14) et des tokens (générés par 2-5).

## Variables

| Variable | Valeur | Remplie par |
|----------|--------|-------------|
| `base_url` | `http://localhost:8081` | Manuel |
| `kc_url` | `http://localhost:8080` | Manuel |
| `client_secret` | `**********` | Manuel |
| `adminToken` | *(auto)* | Requête 5 |
| `managerToken` | *(auto)* | Requête 4 |
| `technicianToken` | *(auto)* | Requête 3 |
| `userToken` | *(auto)* | Requête 2 |
| `testClientId` | *(auto)* | Requête 14 |

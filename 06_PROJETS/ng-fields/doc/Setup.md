# Kit de Démarrage — NG-Fields

**Stack :** Spring Boot 4.0.6 / Java 25 + Flutter/Dart + Angular/TypeScript + Keycloak 26.6.2 + Supabase

---

## Prérequis

| Outil | Version mini | Installation |
|-------|-------------|--------------|
| Java | 25 | [Java Download](https://jdk.java.net/25/) |
| Maven | Wrapper (inclus) | Via `apps/ng-fields-api/mvnw` |
| Node.js | 20 LTS | [Node.js](https://nodejs.org/) |
| Angular CLI | 18+ | `npm install -g @angular/cli` |
| Flutter | 3.x | [Flutter SDK](https://docs.flutter.dev/get-started/install) |
| Docker (optionnel) | — | Pour Redis local |

> **Problème Docker ?** PostgreSQL est sur Supabase Cloud (pas de Docker nécessaire).
> Alternatives : installer Redis natif Windows, lancer Keycloak en JAR standalone.

---

## 1. Cloner le dépôt

```bash
git clone https://github.com/moltesse12/ng-fields.git
cd ng-fields
```

---

## 2. Configuration Supabase

Créer un projet gratuit sur [https://supabase.com](https://supabase.com).

```bash
cp .env.example .env
# Éditer .env avec les credentials Supabase
```

Variables requises dans `.env` :

```
SUPABASE_DB_HOST=db.xxxxxxxxxxxxx.supabase.co
SUPABASE_DB_USER=postgres
SUPABASE_DB_PASSWORD=your-password
SUPABASE_URL=https://xxxxxxxxxxxxx.supabase.co
SUPABASE_ANON_KEY=eyJhbGci...
KEYCLOAK_URL=http://localhost:8080
KEYCLOAK_REALM=ng-fields
JWT_SECRET=<dev-secret>
```

---

## 3. Backend (Spring Boot)

### 3.1 Démarrer les services requis

**Option A — Docker (recommandé si fonctionnel) :**
```bash
docker compose -f infra/docker-compose.yml up -d
```

**Option B — Sans Docker :**
- **Redis** : Télécharger [Redis pour Windows](https://github.com/microsoftarchive/redis/releases) ou utiliser WSL
- **Keycloak** : Télécharger [Keycloak 26.6.2](https://www.keycloak.org/downloads), extraire et lancer :
  ```bash
  cd apps/keycloak-26.6.2/bin
  ./kc.bat start-dev
  ```

### 3.2 Lancer l'API

```bash
cd apps/ng-fields-api
./mvnw spring-boot:run
```

L'API démarre sur `http://localhost:8081`.

### 3.3 Tests

```bash
./mvnw clean verify
```

### 3.4 Migrations Flyway

Les migrations s'exécutent automatiquement au démarrage :
- `V1__init.sql` — schéma initial (tables)
- `V2__add_indexes.sql` — index de performance
- `V3__add_gps_fields.sql` — champs GPS (V0.1)

---

## 4. Mobile (Flutter)

```bash
cd apps/mobile
flutter pub get
flutter run
```

### Dépendances principales

| Package | Usage |
|---------|-------|
| flutter_riverpod | State management |
| go_router | Navigation |
| dio | HTTP client |
| drift + sqlite3_flutter_libs | Base locale offline |
| flutter_secure_storage | Stockage tokens |
| image_picker | Camera/photos |
| geolocator | GPS |
| signature | Signature tactile |
| firebase_messaging | Notifications push (V1) |
| connectivity_plus | Détection réseau |

---

## 5. Web (Angular)

```bash
cd apps/web
npm install
ng serve
```

Le dashboard démarre sur `http://localhost:4200`.

### Dépendances principales

| Package | Usage |
|---------|-------|
| @angular/material | UI components |
| angular-auth-oidc-client | Authentification OIDC |
| chart.js / ngx-charts | Graphiques |
| @angular/cdk | Drag & drop planning |

---

## 6. Structure du projet

```
ng-fields/
├── apps/
│   ├── ng-fields-api/          → Backend Spring Boot (Java 25)
│   │   ├── src/main/java/com/ngstars/
│   │   ├── pom.xml
│   │   └── mvnw
│   ├── mobile/                  → App Flutter (Dart)
│   └── web/                     → Dashboard Angular (TS)
├── infra/
│   ├── docker-compose.yml       → Redis + services
│   ├── supabase/
│   │   ├── schema.sql           → Schéma BDD
│   │   └── seed.sql             → Données de test
│   └── keycloak/
│       └── realm-export.json    → Realm exporté
├── docs/
│   ├── architecture/
│   ├── database/
│   ├── mobile/
│   ├── integrations/
│   └── tests/
├── .github/workflows/
│   ├── backend.yml              → CI Spring Boot
│   └── mobile.yml               → CI Flutter
├── Backlog.md                   → Backlog produit
├── Technologies.md              → Stack technique
├── Roadmap.md                   → Planning versions
└── README.md                    → Présentation projet
```

---

## 7. Commandes utiles

```bash
# Backend
cd apps/ng-fields-api && ./mvnw spring-boot:run    # Lancer
cd apps/ng-fields-api && ./mvnw clean verify        # Tester

# Mobile
cd apps/mobile && flutter pub get                   # Dépendances
cd apps/mobile && flutter run                       # Lancer
cd apps/mobile && flutter test                      # Tests
cd apps/mobile && flutter build apk                 # APK Android

# Web
cd apps/web && npm install                           # Dépendances
cd apps/web && ng serve                              # Lancer
cd apps/web && ng build --prod                       # Build production

# Infra
docker compose -f infra/docker-compose.yml up -d    # Démarrer services
```

---

## 8. Documentation API

```bash
# Swagger UI
http://localhost:8081/swagger-ui.html

# Spécification OpenAPI JSON
http://localhost:8081/v3/api-docs
```

---

_Version 2.0 — 03/06/2026_

# NG-Fields

Digitalisation de la gestion des interventions terrain pour NG-STARs.

**Stack :** Spring Boot 4.0.6 / Java 25 + Flutter/Dart + Angular/TypeScript + Keycloak 26.6.2 + PostgreSQL/Supabase

---

## Structure

```
apps/
├── ng-fields-api/          Backend Spring Boot
├── mobile/                 App Flutter
└── web/                    Dashboard Angular

infra/
├── docker-compose.yml      Redis + services
├── supabase/               Schéma BDD + données test
└── keycloak/               Realm export

docs/                       Documentation
.github/workflows/          CI/CD
```

---

## Démarrage rapide

```bash
# 1. Cloner
git clone https://github.com/moltesse12/ng-fields.git
cd ng-fields

# 2. Variables d'environnement
cp .env.example .env

# 3. Redis (via Docker ou natif)
docker compose -f infra/docker-compose.yml up -d

# 4. Backend
cd apps/ng-fields-api
./mvnw spring-boot:run

# 5. Mobile (autre terminal)
cd apps/mobile
flutter run
```

---

## Documentation API

Swagger UI : `http://localhost:8081/swagger-ui.html`

---

## Versions

| Version | Période | Livrables |
|---------|---------|-----------|
| **V0** | 1-12 juin | Auth + Core (API + Mobile) |
| **V0.1** | 13-26 juin | Mobile complet + Envoi |
| **V1** | 29 juin+ | Dashboard web + Intégrations |

---

## Licence

Propriétaire — NG-STARs

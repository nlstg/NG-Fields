# NG-Fields — Contexte pour agents IA

## Stack
- **Backend:** Spring Boot 4.0.6 / Java 25, Maven, JPA/Hibernate
- **Auth:** Keycloak 26.6.2 (OAuth2/OpenID Connect)
- **Mobile:** Flutter/Dart (Riverpod, GoRouter, Drift/SQLite)
- **DB:** PostgreSQL via Supabase
- **PDF:** OpenPDF + ZXing (QR codes)
- **CI/CD:** GitHub Actions

## Structure des dossiers

| Dossier | Contenu |
|---------|---------|
| `apps/ng-fields-api/` | Backend Spring Boot (code existant) |
| `apps/mobile/` | App Flutter (à créer) |
| `infra/` | Docker Compose, schémas BDD, config Keycloak |
| `docs/` | Documentation organisée par thème |
| `wireframes/` | Maquettes UI |
| `scripts/` | Scripts dev/build/deploy |
| `.github/workflows/` | CI (backend.yml, mobile.yml) |

## Règles de codage
- Toujours utiliser des DTOs (records) pour les entrées/sorties API
- Injections par constructeur (pas de @Autowired)
- Les entités JPA utilisent `@PrePersist`/`@PreUpdate` pour les timestamps
- L'ID client `localId` sert à l'idempotence de synchronisation offline→online
- La génération PDF utilise OpenPDF (pas iText)
- Toujours logger les actions importantes

## Conventions git
- Branches: `feature/US-*`, `fix/US-*`, `release/v*.*`
- Commits en français (convention du projet)
- Ne pas tracker: Keycloak dist, fichiers build, .md à la racine, wireframes

## Commandes
```bash
# Backend
cd apps/ng-fields-api && ./mvnw spring-boot:run   # Lancer
cd apps/ng-fields-api && ./mvnw clean verify       # Tester + builder

# Mobile (quand prêt)
cd apps/mobile && flutter pub get                   # Dépendances
cd apps/mobile && flutter run                       # Lancer sur device
cd apps/mobile && flutter test                      # Tests

# Infra
docker compose -f infra/docker-compose.yml up -d    # Démarrer services
```

## Décisions d'architecture
- **Pourquoi Keycloak et pas Supabase Auth** : découplage auth/DB, compatible multi-projets
- **Pourquoi Spring Boot et pas Node.js** : stack Java existante dans l'entreprise
- **Pourquoi Offline first** : couverture réseau aléatoire sur le terrain
- **Pourquoi Drift/SQLite** : seule solution Flutter mature avec migrations typées

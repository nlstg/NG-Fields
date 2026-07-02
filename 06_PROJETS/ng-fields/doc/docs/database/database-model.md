# Architecture PostgreSQL — NG-Fields

**Révision :** 1.0 — Juin 2026
**Moteur :** PostgreSQL 18 (localhost:5432)

---

## 1. Bases de données

```
┌────────────────────────────────────────────────────────────────┐
│               PostgreSQL 18 (localhost:5432)                     │
├──────────────────┬──────────────────────┬───────────────────────┤
│     keycloak     │      ng_fields       │    ng_fields_test      │
│  (keycloak_user) │  (ng_fields_user)    │ (ng_fields_test_user)  │
├──────────────────┼──────────────────────┼───────────────────────┤
│ Base dédiée à    │ Base principale      │ Base de tests         │
│ Keycloak IAM     │ de l'API             │ (recréée à chaque     │
│ (gère ~80 tables │ Spring Boot          │ run des tests         │
│  elle-même)      │ avec 5 schémas       │ d'intégration)        │
│                  │ métier               │                       │
└──────────────────┴──────────────────────┴───────────────────────┘
```

## 2. Rôles applicatifs

| Rôle | Base | Connection Limit | Mot de passe | Usage |
|---|---|---|---|---|
| `ng_fields_user` | `ng_fields` | 50 | `Pg_ng-fields1234` | API Spring Boot |
| `keycloak_user` | `keycloak` | 20 | `Keycloak_Pg_2026!` | Keycloak IAM |
| `ng_fields_test_user` | `ng_fields_test` | 10 | `NgFieldsTest_Pg_2026!` | Tests d'intégration |

> **Règle :** Jamais le superuser `postgres` dans les applications.

## 3. Paramètres de connexion

| Composant | JDBC URL | Base | User |
|---|---|---|---|
| API Spring Boot (dev) | `jdbc:postgresql://localhost:5432/ng_fields` | `ng_fields` | `ng_fields_user` |
| API Spring Boot (test) | `jdbc:postgresql://localhost:5432/ng_fields_test` | `ng_fields_test` | `ng_fields_test_user` |
| Keycloak | `jdbc:postgresql://localhost:5432/keycloak` | `keycloak` | `keycloak_user` |

## 4. Documentation par base

| Base | Document | Contenu |
|---|---|---|
| `ng_fields` | [ng-fields-application.md](ng-fields-application.md) | 5 schémas, 7 tables, DDL complet, mapping JPA |
| `keycloak` | [keycloak.md](keycloak.md) | Driver, vars d'env, démarrage, notes Liquibase |
| `ng_fields_test` | [ng-fields-application.md](ng-fields-application.md) | Identique à `ng_fields` (miroir) |

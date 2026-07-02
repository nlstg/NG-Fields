# Flux de données — NG-Fields

## En ligne

```
[Mobile Flutter] ←→ [Spring Boot API] ←→ [PostgreSQL (Supabase)]
                        ↕                       ↕
                   [Keycloak Auth]        [Supabase Storage]
                        ↕
              [OpenProject API] / [SendGrid] / [Twilio]
```

## Hors-ligne (offline)

```
[Mobile Flutter]
    ├── Drift (SQLite) ← stockage local
    ├── Queue sync → [Spring Boot API] (quand connecté)
    └── Photos en cache local
```

### Synchronisation
1. L'app mobile écrit en local (Drift) + file d'attente
2. À la reconnexion : envoi batch via `POST /api/interventions/sync`
3. Le serveur détecte les doublons via `localId` (idempotence)
4. Résolution de conflits : **server wins** (la version serveur écrase)

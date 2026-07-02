# Supabase Migrations

Les migrations SQL sont gérées via Supabase Studio ou `supabase CLI`.

Documentation : https://supabase.com/docs/guides/cli/migrations

## Commandes

```bash
# Init Supabase project
supabase init

# Link to remote project
supabase link --project-ref <ref>

# Pull remote schema
supabase db pull

# Create a new migration
supabase migration new <nom_migration>

# Apply migrations
supabase db push
```

## Structure

Le schéma PostgreSQL est défini dans `apps/api/src/main/resources/` via Hibernate DDL (validate uniquement les dev).
Les migrations suivent le modèle Prisma actuel (voir `apps/api/prisma/schema.prisma`).

## Storage Buckets

- `intervention-photos` : Photos avant/après (publique en lecture, authentifié en écriture)
- `signatures` : Signatures clients (privé, accès API uniquement)
- `reports` : Rapports PDF générés (privé, accès API uniquement)

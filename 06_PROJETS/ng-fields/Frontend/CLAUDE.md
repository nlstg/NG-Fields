# Frontend — NG-STARs

## Projet

NG-STARs est la plateforme de gestion terrain pour NG-STARs (NG-Fields). Elle comprend :

- **`ng-web/`** — Application Angular (dashboard, auth Keycloak, sidebar, thème)
- **`templates/next-shadcn-admin-dashboard-main/`** — Template Next.js du dashboard NG-STARs

## Branding NG-STARs

| Élément | Valeur |
|---------|--------|
| Nom | NG-STARs |
| Primaire | `#40946e` (vert) |
| Accent | `#0077a7` (bleu) |
| Fond | `#171819` (noir) |
| Police | Titillium Web |

### Variables CSS (OKLCH)
- `--primary: oklch(0.55 0.09 160)` (vert)
- `--chart-2 / accent: oklch(0.45 0.12 250)` (bleu)
- `--background / --foreground: oklch(0.18 0 0)` / `oklch(0.95 0 0)` (noir)

## Structure template Next.js

```
src/
├── app/                          # Pages et layouts
│   ├── (external)/               # Pages publiques (redirect → dashboard)
│   ├── (main)/                   # Pages authentifiées
│   │   ├── auth/                 # Login v1/v2, Register v1/v2
│   │   ├── dashboard/            # Dashboard layout + toutes les screens
│   │   │   ├── _components/      # Composants partagés du dashboard
│   │   │   │   └── sidebar/      # Sidebar (app-sidebar, nav-main, nav-user…)
│   │   │   ├── default/          # Dashboard principal (Overview)
│   │   │   ├── crm/, finance/, analytics/, etc.
│   │   │   └── (legacy)/         # Dashboard legacy V1
│   │   └── unauthorized/
│   ├── globals.css               # Variables CSS, thème, presets
│   └── layout.tsx                # Root layout (html attrs, providers)
├── components/ui/                # Composants shadcn (ne pas modifier)
├── config/
│   └── app-config.ts             # APP_CONFIG (name, version, meta)
├── lib/
│   ├── fonts/registry.ts         # 18 polices Google Fonts
│   └── preferences/             # Layout, thème, préférences
├── navigation/sidebar/
│   └── sidebar-items.ts          # Tous les items de la sidebar
├── scripts/
│   └── theme-boot.tsx            # Script inline anti-flicker
├── stores/preferences/          # Zustand store préférences
└── styles/presets/              # brutalist.css, soft-pop.css, tangerine.css
```

## Commandes

### Template Next.js
```bash
cd templates/next-shadcn-admin-dashboard-main
npm install
npm run dev       # → http://localhost:3000
npm run build
npm run lint
npm run format
npm run check
npm run generate:presets
```

### Angular ng-web
```bash
cd ng-web
npm install
npm start         # → http://localhost:4200
npm run build
```

## Conventions

- **Template Next.js** : ne pas modifier `src/components/ui/` (composants shadcn intacts)
- **Code colocalisé** : garder les composants proches de leur route
- **TypeScript strict** : types précis, pas de `any`
- **Biome** : double quotes, point-virgules, 2 espaces, 120 caractères, imports triés
- **Palette** : utiliser les tokens sémantiques (`--color-primary`, `--color-muted`, etc.), pas de valeurs hex/oklch en dur
- **shadcn** : style `radix-nova` (vérifier `components.json`)

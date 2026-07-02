# 12 — Dashboard et Statistiques

**Objectif :** API REST fournissant les données du tableau de bord manager
**Dépend de :** [06-intervention-crud.md](06-intervention-crud.md)

---

## Endpoints

| Méthode | Endpoint | Rôle | Description |
|---------|----------|------|-------------|
| GET | `/api/manager/dashboard` | MANAGER, ADMIN | KPIs du jour |
| GET | `/api/manager/stats/by-technician` | MANAGER, ADMIN | Stats par technicien |
| GET | `/api/manager/stats/by-client` | MANAGER, ADMIN | Stats par client |
| GET | `/api/manager/export/csv` | MANAGER, ADMIN | Export CSV |
| GET | `/api/manager/export/excel` | MANAGER, ADMIN | Export Excel |
| GET | `/api/interventions/stats` | MANAGER, ADMIN | Agrégations |

## Guide détaillé

> **À rédiger après Sprint 4**

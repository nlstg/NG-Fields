# OpenProject API v3 — Référence intégration NG-Fields

Source : https://github.com/opf/openproject/tree/dev/docs/api + https://www.openproject.org/docs/api/

---

## Authentification

| Méthode | Détail |
|---------|--------|
| **API key via Basic Auth** | Username: `apikey`, Password: (la clé générée dans le profil OpenProject) |
| **API key via Bearer** | Header: `Authorization: Bearer <api_key>` |
| **OAuth 2.0** | Authorization server intégré ou externe (RFC 9068) |

Exemple Basic Auth :
```bash
curl -u apikey:opapi-2519132cdf62dcf5a66fd96394672079f9e9cad1 \
  https://instance.openproject.com/api/v3/work_packages
```

---

## Endpoints clés

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/v3` | Entry point HATEOAS (découverte) |
| `GET` | `/api/v3/projects` | Liste des projets |
| `GET` | `/api/v3/projects/{id}` | Détail projet |
| `POST` | `/api/v3/projects/{id}/work_packages` | Créer work package dans projet |
| `POST` | `/api/v3/work_packages` | Créer work package (générique) |
| `POST` | `/api/v3/work_packages/form` | Formulaire création (validation + schéma) |
| `GET` | `/api/v3/work_packages/{id}` | Détail work package |
| `PATCH` | `/api/v3/work_packages/{id}` | Mettre à jour work package |
| `DELETE` | `/api/v3/work_packages/{id}` | Supprimer work package |
| `GET` | `/api/v3/statuses` | Liste des statuts possibles |
| `GET` | `/api/v3/types` | Liste des types de work packages |
| `GET` | `/api/v3/priorities` | Liste des priorités |

---

## Création d'un ticket (work package)

### Requête minimale
```bash
curl -u apikey:TOKEN \
  -X POST https://instance.openproject.com/api/v3/work_packages \
  -H 'Content-Type: application/json' \
  -d '{
    "subject": "Demande intervention client - NOM_CLIENT - DATE",
    "_links": {
      "project": { "href": "/api/v3/projects/12" },
      "type":    { "href": "/api/v3/types/1" },
      "status":  { "href": "/api/v3/statuses/1" },
      "priority":{ "href": "/api/v3/priorities/2" }
    }
  }'
```

### Champs disponibles
| Champ | Type | Obligatoire | Description |
|-------|------|-------------|-------------|
| `subject` | string (255) | Oui | Titre du ticket |
| `description` | object (raw) | Non | Contenu au format CommonMark |
| `startDate` | date | Non | Date de début |
| `dueDate` | date | Non | Date d'échéance |
| `duration` | string (ISO 8601) | Non | Durée (ex: `P1D`, `P3D`) |
| `_links.project` | HAL link | Oui | Projet parent |
| `_links.type` | HAL link | Non | Type de WP |
| `_links.status` | HAL link | Non | Statut initial |
| `_links.priority` | HAL link | Non | Priorité |
| `_links.assignee` | HAL link | Non | Assigné à |
| `_links.responsible` | HAL link | Non | Responsable |
| `_links.parent` | HAL link | Non | WP parent |
| `_links.version` | HAL link | Non | Version/sprint |

### Workflow recommandé (form + commit)
```
POST /api/v3/work_packages/form
Body: { subject: "...", _links: { project: {...} } }
  → Response: { _embedded: { payload: {...}, schema: {...}, validationErrors: {...} } }
  → Si pas d'erreurs → suivre _links.commit.href
  → POST vers commit URL avec payload comme body
```

---

## Codes réponse

| Code | Signification |
|------|---------------|
| 200 | Succès (GET, PATCH) |
| 201 | Créé (POST) |
| 401 | Non authentifié |
| 403 | Non autorisé |
| 404 | Ressource introuvable |
| 422 | Erreur validation (champs requis, contraintes) |

---

## Erreurs (422)
```json
{
  "_type": "Error",
  "errorIdentifier": "urn:openproject-org:api:v3:errors:PropertyConstraintViolation",
  "message": "Subject can't be blank."
}
```

---

## Pagination
Paramètres : `?offset=1&pageSize=100` (défaut: 30, max: 1000)

---

## Filtres
```json
// GET /api/v3/work_packages?filters=[...]
// Encodé en JSON string dans l'URL
[{"status": {"operator": "=", "values": ["1"]}}]
```

---

## Statuts par défaut (IDs OpenProject)
| ID | Statut |
|----|--------|
| 1 | New |
| 2 | In Progress |
| 3 | Resolved |
| 4 | Closed |
| 5 | Rejected |

> **⚠️ Important** : Les IDs exacts (projets, types, statuts, priorités) dépendent de l'instance OpenProject cible.
> À récupérer dynamiquement via `/api/v3/projects`, `/api/v3/statuses`, `/api/v3/types`, `/api/v3/priorities`.

---

## Références
- OpenAPI spec : `/api/v3/spec.json` (sur l'instance) ou https://www.openproject.org/docs/api/v3/spec.json
- Guide : https://www.openproject.org/docs/api/example/
- Endpoints : https://www.openproject.org/docs/api/endpoints/
- GitHub source : https://github.com/opf/openproject/tree/dev/docs/api

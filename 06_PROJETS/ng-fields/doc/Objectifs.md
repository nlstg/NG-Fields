---
tags:
  - projet
  - ng-fields
  - objectifs
created: 2026-06-03
status: v2
---

# Objectifs — NG-Fields

## Objectif Principal

**Digitaliser et centraliser la gestion des interventions terrain de NG-STARs** :

1. Éliminer les fiches papier FI-01-2025 et la perte de documents
2. Garantir la traçabilité complète de chaque intervention
3. Connaître le temps précis passé sur site par intervention
4. Améliorer la réactivité du management par des notifications temps réel
5. Fournir des données exploitables pour le pilotage

---

## Phases de livraison

| Version | Période | Objectif | Livrables clés |
|---------|---------|----------|----------------|
| **V0** | 1-12 juin | Auth & Core | API stable, app mobile avec auth, dashboard tech, CRUD clients/interventions |
| **V0.1** | 13-26 juin | Mobile & Envoi | Photos, signatures, PDF, mode hors-ligne, email/WhatsApp |
| **V1** | 29 juin+ | Web & Pro | Dashboard Angular, portail client, OpenProject, notifications |

---

## Objectifs SMART

### 1 — Connaître le temps précis passé sur site

| Champ | Valeur |
|-------|--------|
| Spécifique | Capturer auto heure arrivée, début, fin, retour |
| Mesurable | Minutes exactes via horodatage + GPS |
| Atteignable | Oui (horodatage mobile) |
| Réaliste | Oui |
| Temporel | V0 (12 juin) — saisie API ; V0.1 (26 juin) — capture mobile |

### 2 — Réduction du temps de saisie

| Champ | Valeur |
|-------|--------|
| Spécifique | Réduire la saisie de ~15-20 min à 5 min max |
| Mesurable | Temps moyen mesuré via logs |
| Atteignable | Oui (auto-complétion, calcul auto) |
| Réaliste | Oui |
| Temporel | Cible 3 mois post-déploiement |

### 3 — Élimination de la perte de données

| Champ | Valeur |
|-------|--------|
| Spécifique | 100% des interventions tracées (vs ~50% aujourd'hui) |
| Mesurable | % enregistrées vs réelles |
| Atteignable | Oui (mode hors-ligne + saisie numérique obligatoire) |
| Réaliste | Oui |
| Temporel | V0.1 (26 juin) — avec le mode hors-ligne |

### 4 — Amélioration de la réactivité management

| Champ | Valeur |
|-------|--------|
| Spécifique | Réduire le temps de réponse de 24-48h à temps réel |
| Mesurable | Délai fin intervention → notification manager |
| Atteignable | Oui (notifications push + email) |
| Réaliste | Oui |
| Temporel | V1 (mi-juillet) |

### 5 — Productivité techniciens

| Champ | Valeur |
|-------|--------|
| Spécifique | +20% d'interventions/jour |
| Mesurable | Nombre moyen/technicien/jour |
| Atteignable | Oui (gain ~30-45 min/jour) |
| Réaliste | Oui |
| Temporel | 3 mois post-déploiement |

### 6 — Portail client & intégration OpenProject

| Champ | Valeur |
|-------|--------|
| Spécifique | Client soumet sa demande → ticket OpenProject créé automatiquement |
| Mesurable | Temps création ticket (< 30s), taux succès (> 95%) |
| Atteignable | Oui (API REST + file attente Redis) |
| Réaliste | Oui |
| Temporel | V1 (juillet) |

---

## KPIs

| Objectif | KPI | Baseline | Cible | Mesure |
|----------|-----|----------|-------|--------|
| Temps sur site | Minutes précises | Non mesuré | 100% tracé | Par intervention |
| Temps de saisie | Min/intervention | ~15-20 min | 5 min (3 mois) | Mensuel |
| Perte données | % perdues | ~15% | 0% | Mensuel |
| Interventions tracées | % | ~50% | 100% | Mensuel |
| Temps réponse manager | Heures | 24-48h | Temps réel (V1) | Hebdo |
| Productivité tech. | Int./jour | À mesurer | +20% (6 mois) | Mensuel |
| Usage offline | % offline | 0% | 50% (3 mois) | Mensuel |
| Signature num. | % | 0% | 100% | Hebdo |
| Portail client | Tickets créés/mois | 0 | > 10 (3 mois) | Mensuel |
| OpenProject | Temps création ticket | N/A | < 30s | Hebdo |

---

## Critères de succès (GO-LIVE V1)

| # | Critère | Validateur | Version |
|---|---------|------------|---------|
| 1 | App Android + iOS fonctionnelle | David KATOH | V0 |
| 2 | Temps sur site tracé à 100% | David KATOH | V0 |
| 3 | Formulaire FI-01-2025 complet + signatures | Barnabé | V0.1 |
| 4 | Mode hors-ligne fonctionnel | Barnabé | V0.1 |
| 5 | PDF avec logo, photos, signatures, QR | David KATOH | V0.1 |
| 6 | Envoi email/WhatsApp | Barnabé | V0.1 |
| 7 | Dashboard manager Angular | David KATOH | V1 |
| 8 | Portail client (soumission → ticket OpenProject) | David KATOH | V1 |
| 9 | UAT validé par 3 techniciens | Barnabé | V1 |
| 10 | Conformité RGPD | David KATOH | V0 |
| 11 | Formation terminée | Barnabé | V1 |

---

_Version 2.0 — 03/06/2026_

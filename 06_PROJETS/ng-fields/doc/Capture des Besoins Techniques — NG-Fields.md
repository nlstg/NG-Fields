**Projet :** NG-Fields – Digitalisation de la gestion des interventions terrain  
**Référence CdC :** NG-STARs Cahier des Charges V4-Final  
**Rédigé par :** FOLLY Nelson Emmanuel (Stagiaire)  
**Validateur :** David KATOH (Responsable IT)  
**Version :** 1.0  
**Date :** 01/06/2026

---

## Sommaire

1. [Introduction](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#1-introduction)
2. [Architecture technique du système](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#2-architecture-technique-du-syst%C3%A8me)
3. [Contraintes techniques](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#3-contraintes-techniques)
4. [Technologies retenues](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#4-technologies-retenues)
5. [Déploiement](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#5-d%C3%A9ploiement)
6. [Interopérabilité et systèmes externes](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#6-interop%C3%A9rabilit%C3%A9-et-syst%C3%A8mes-externes)
7. [Plan de supervision et de journalisation](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#7-plan-de-supervision-et-de-journalisation)
8. [Sécurité système]()
9. [Diagramme de déploiement](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#9-diagramme-de-d%C3%A9ploiement)
10. [Conclusion](https://claude.ai/chat/a3b4bf48-1188-44e2-9e03-6f116f57474b#10-conclusion)

---

## 1. Introduction

### 1.1 Objet du document

Ce document constitue la capture des besoins techniques du projet NG-Fields. Il complète la capture des besoins fonctionnels en précisant les exigences d'architecture, de technologies, de déploiement, de sécurité et de supervision nécessaires à la réalisation du système. Il sert de référence technique commune pour l'équipe de développement senior de NG-STARs tout au long du cycle de vie du projet.

### 1.2 Périmètre

Le système NG-Fields se compose de quatre composantes complémentaires :

- une **application mobile** (iOS et Android) dédiée aux techniciens terrain ;
- un **tableau de bord web** accessible aux managers et administrateurs ;
- un **portail client** simplifié pour la soumission de demandes d'intervention ;
- un **backend centralisé** (API REST) assurant la logique métier, la persistance, la génération des PDF, la synchronisation hors-ligne et les notifications.

### 1.3 Références

|Document|Version|Date|
|---|---|---|
|Cahier des charges SDCGI|V4-Final|10/05/2026|
|Capture des besoins fonctionnels|1.0|01/06/2026|
|Document UML par la pratique (P. Roques)|5e édition|2006|

---

## 2. Architecture technique du système

### 2.1 Vue d'ensemble

L'architecture de NG-Fields est organisée selon le modèle **client-serveur multi-tiers**, avec un découplage clair entre la couche présentation (mobile et web), la couche métier (API REST) et la couche de persistance (base de données relationnelle). Le backend expose une API REST sécurisée via JWT, consommée aussi bien par l'application mobile que par le tableau de bord web.

```
┌─────────────────────────────────────────────────────────────────┐
│                        COUCHE PRÉSENTATION                      │
│  ┌──────────────────────┐        ┌──────────────────────────┐   │
│  │  Application Mobile  │        │  Tableau de bord Web /   │   │
│  │  (React Native /     │        │  Portail Client          │   │
│  │   Flutter)           │        │  (React / Next.js)       │   │
│  └──────────┬───────────┘        └───────────┬──────────────┘   │
└─────────────┼────────────────────────────────┼──────────────────┘
              │  HTTPS / JWT                   │  HTTPS / JWT
┌─────────────▼────────────────────────────────▼───────────────────┐
│                         COUCHE MÉTIER (API REST)                 │
│  ┌───────────────────────────────────────────────────────────┐   │
│  │  Backend Node.js + NestJS (ou FastAPI)                    │   │
│  │  ├── Module Auth (JWT + MFA)                              │   │
│  │  ├── Module Interventions                                 │   │
│  │  ├── Module Clients                                       │   │
│  │  ├── Module PDF Generator                                 │   │
│  │  ├── Module Notifications (Push + Email + WhatsApp)       │   │
│  │  ├── Module Sync (offline queue)                          │   │
│  │  └── Module OpenProject (API REST v3)                     │   │
│  └───────────────────────────────────────────────────────────┘   │
└─────────────────────────────┬────────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────┐
│                       COUCHE PERSISTANCE                        │
│  ┌──────────────┐  ┌───────────────┐  ┌──────────────────────┐  │
│  │  PostgreSQL  │  │     Redis     │  │  Stockage objets     │  │
│  │  (Supabase)  │  │  (Cache/Queue)│  │  (Photos, PDFs)      │  │
│  └──────────────┘  └───────────────┘  └──────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 Principes d'architecture

L'architecture du système respecte les principes suivants :

**Offline-First.** L'application mobile est conçue selon une logique offline-first. Toutes les données saisies (formulaire, photos, signatures, coordonnées GPS) sont d'abord persistées localement dans une base SQLite chiffrée, puis synchronisées automatiquement avec le backend dès le retour de la connectivité, dans un délai maximum de 60 secondes.

**Séparation des responsabilités.** Le backend suit une architecture en trois couches (Controller → Service → Repository), chaque module fonctionnel possédant ses propres classes de traitement, conformément aux bonnes pratiques de la conception orientée objet décrites dans le document UML de référence.

**Stateless.** L'API REST est sans état. Chaque requête est authentifiée indépendamment via un jeton JWT à durée de vie courte, sans maintien de session côté serveur.

**Modularité.** Chaque domaine fonctionnel (interventions, clients, notifications, PDF, synchronisation, OpenProject) est encapsulé dans un module indépendant, facilitant la maintenance et l'évolution progressive du système.

### 2.3 Composantes de l'application mobile

L'application mobile comporte les couches internes suivantes :

| Couche               | Rôle                                                                |
| -------------------- | ------------------------------------------------------------------- |
| **UI Layer**         | Écrans et composants Flutter                                        |
| **State Management** | Zustand / Riverpod — gestion de l'état global                       |
| **Service Layer**    | Logique métier locale (calcul durée, validation, compression photo) |
| **Sync Service**     | File d'attente hors-ligne, détection réseau, envoi différé          |
| **Local DB**         | SQLite chiffrée via SQLCipher / WatermelonDB Encrypted              |
| **API Client**       | Appels HTTP vers le backend, gestion des tokens JWT                 |

### 2.4 Composantes du backend

|Module|Responsabilité principale|
|---|---|
|`AuthModule`|Authentification JWT, MFA, gestion des rôles (ADMIN, MANAGER, TECHNICIAN)|
|`InterventionModule`|CRUD des fiches d'intervention, calcul durée, gestion du statut|
|`ClientModule`|CRUD des fiches client, historique|
|`PdfModule`|Génération automatique des rapports PDF (< 10 s)|
|`NotificationModule`|Push (FCM/APNs), Email (SMTP/SendGrid), WhatsApp Business API|
|`SyncModule`|Réception et résolution des conflits des fiches hors-ligne|
|`OpenProjectModule`|Création et mise à jour des tickets via l'API REST v3|
|`StorageModule`|Gestion du stockage des photos et des PDFs générés|

---

## 3. Contraintes techniques

### 3.1 Contraintes de connectivité

L'analyse des conditions réelles d'intervention NG-STARs met en évidence les niveaux de connectivité suivants :

| Zone                          | Qualité réseau                    | Fréquence estimée          |
| ----------------------------- | --------------------------------- | -------------------------- |
| Siège et bureaux              | Excellente, stable                | Synchronisation temps réel |
| Zones urbaines (centre-ville) | Bonne — interruptions ponctuelles | ~70 % des interventions    |
| Zones périphériques           | Moyenne à faible                  | ~30 % des interventions    |
| Zones rurales                 | Faible à nulle                    | ~20 % des interventions    |

Le mode hors-ligne est donc une **exigence fondamentale et non optionnelle**, couvrant 100 % des scenarios d'intervention. La solution est conçue selon la logique **offline-first** : toute fonctionnalité accessible en mode connecté doit l'être également hors connexion (saisie du formulaire, photos, GPS, signatures).

### 3.2 Contraintes de performance

|Indicateur|Valeur cible|
|---|---|
|Génération du rapport PDF|≤ 10 secondes|
|Synchronisation hors-ligne au retour réseau|≤ 60 secondes|
|Délai de réception des notifications push|≤ 1 minute|
|Délai d'envoi email du rapport|≤ 2 minutes|
|Création d'un ticket OpenProject depuis le portail client|≤ 30 secondes|
|Temps de réponse de l'API REST (opérations courantes)|≤ 2 secondes|
|Compression photo (préservation métadonnées GPS)|Cible ≤ 500 Ko par photo|

### 3.3 Contraintes de compatibilité

- L'application mobile doit fonctionner sur **iOS 14+** et **Android 10+**.
- Le tableau de bord web doit être accessible depuis les navigateurs modernes sans installation : Chrome, Firefox, Edge et Safari (versions N et N-1).
- L'API REST doit respecter le standard **OpenAPI 3.0** pour faciliter l'intégration et la documentation.
- L'intégration OpenProject doit être compatible avec l'**API REST v3** de la version Community Edition.

### 3.4 Contraintes de capacité

- Minimum **15 techniciens** utilisateurs simultanés de l'application mobile.
- Minimum **10 managers** utilisateurs simultanés du tableau de bord web.
- Minimum **10 administrateurs IT** pour la gestion du système.
- Jusqu'à **10 photos par intervention** (5 avant + 5 après), stockées avec leurs métadonnées.
- Les sauvegardes automatiques quotidiennes doivent couvrir une **rétention de 30 jours minimum**.

### 3.5 Contraintes budgétaires

Le budget mensuel disponible est fixé à **12 000 XOF (~18 €)**, orientant le choix vers des solutions open-source et des hébergements économiques :

|Poste|Coût estimé / mois|
|---|---|
|Hébergement VPS (Hetzner ou équivalent)|5 – 10 €|
|Supabase (tier gratuit ou Pro)|0 – 25 €|
|Abonnement Claude Pro (assistance IA développement)|20 €|
|**Total estimé**|**25 – 35 € / mois**|

---

## 4. Technologies retenues

Les choix technologiques ont été arrêtés selon les critères prioritaires suivants : qualité du support offline-first (critère éliminatoire), maturité de l'écosystème, maîtrise par l'équipe senior NG-STARs, licences open-source, et compatibilité iOS/Android depuis une base de code unique.

### 4.1 Application mobile

|Critère|React Native|Flutter|
|---|---|---|
|Langage|TypeScript / JavaScript|Dart|
|Support offline|Excellent (WatermelonDB, SQLite)|Excellent (Drift, Hive)|
|Performances|Near-native|Très élevées, UI cohérente|
|Taille communauté|Très large|Large et croissante|
|Maîtrise équipe|À confirmer en Sprint 0|À confirmer en Sprint 0|

**Bibliothèques clés :**

| Bibliothèque                      | Usage                                           |
| --------------------------------- | ----------------------------------------------- |
| SQLite + SQLCipher                | Base de données locale chiffrée (offline-first) |
| TanStack                          | Gestion du cache et synchronisation serveur     |
| Expo Camera / react-native-camera | Capture photo avec métadonnées GPS              |
| react-native-signature-canvas     | Zones de signature tactile                      |
| react-native-maps                 | Visualisation de la position GPS                |
| @react-native-async-storage       | Stockage léger des préférences                  |
| NetInfo                           | Détection de l'état de la connexion réseau      |
| Firebase Cloud Messaging (FCM)    | Notifications push Android                      |
| APNs                              | Notifications push iOS                          |

### 4.2 Backend

**Framework retenu : Node.js + NestJS (TypeScript)**

NestJS est privilégié pour son architecture modulaire et maintenable, son support natif TypeScript, sa richesse en décorateurs et son écosystème adapté aux APIs REST robustes.

|Bibliothèque / Outil|Usage|
|---|---|
|NestJS|Framework principal, architecture modulaire|
|Passport.js + JWT|Authentification et gestion des tokens|
|TypeORM / Prisma|ORM pour l'accès à PostgreSQL|
|Puppeteer / PDFKit|Génération des rapports PDF|
|Nodemailer / SendGrid SDK|Envoi d'emails|
|Bull (Redis-based)|File d'attente des tâches asynchrones (sync, PDF, notifs)|
|Firebase Admin SDK|Envoi des notifications push (FCM)|
|Axios|Appels HTTP vers OpenProject API v3|
|class-validator|Validation des DTOs entrants|
|Swagger (OpenAPI 3.0)|Documentation automatique de l'API|

### 4.3 Tableau de bord web et portail client

**Framework retenu : React + Next.js (TypeScript)**

|Bibliothèque|Usage|
|---|---|
|Next.js|Framework React avec SSR et routing|
|Tailwind CSS|Système de design utilitaire|
|Recharts / Chart.js|Visualisation des KPI et statistiques|
|React Table|Tableaux de données avec filtres et exports|
|React Query|Gestion du cache et des données serveur|
|React Hook Form + Zod|Formulaires avec validation côté client|
|xlsx / csv-stringify|Export des données en CSV/Excel|

### 4.4 Base de données

|Technologie|Type|Rôle|
|---|---|---|
|**PostgreSQL** (via Supabase)|Relationnelle SQL|Stockage principal : interventions, clients, utilisateurs, photos, PDFs|
|**SQLite + SQLCipher**|Locale embarquée chiffrée|Stockage mobile hors-ligne (offline-first)|
|**Redis**|Cache en mémoire|Sessions, cache API, file de tâches (Bull queues)|

**Schéma des entités principales :**

```
users           → id, email, password_hash, name, role, department, phone
clients         → id, name, email, phone, address, latitude, longitude
interventions   → id, local_id, date, status, type, technician_id, client_id,
                  duration, problem_desc, diagnosis, work_done, result,
                  recommendations, equipment_*, billable, signature_url,
                  openproject_ticket_id, synced_at, created_at, updated_at
intervention_photos → id, intervention_id, url, type (BEFORE/AFTER), local_path
intervention_items  → id, intervention_id, name, quantity
notifications   → id, recipient_id, title, message, channel, sent_at, read_at
```

### 4.5 Infrastructure et hébergement

**Solution retenue : Supabase + VPS Hetzner**

|Composant|Solution|Justification|
|---|---|---|
|Base de données PostgreSQL|Supabase (free tier → Pro)|PostgreSQL managé, auth intégrée, API REST auto-générée, tier gratuit généreux|
|Serveur d'application|VPS Hetzner CX21 (2 vCPU, 4 Go RAM)|5–10 €/mois, performances suffisantes pour la charge initiale|
|CI/CD|GitHub Actions|Intégration native avec les dépôts GitHub, pipelines automatisés|
|Containerisation|Docker + Docker Compose|Reproductibilité des environnements dev/staging/prod|
|Reverse Proxy|Nginx|Gestion des certificats TLS, load balancing|
|Stockage objets (photos, PDFs)|Supabase Storage ou MinIO auto-hébergé|S3-compatible, gratuit pour les volumes initiaux|

### 4.6 Environnements

Le projet dispose de trois environnements distincts :

|Environnement|Usage|URL type|
|---|---|---|
|`development`|Développement local des développeurs|`localhost:3000`|
|`staging`|Validation interne et tests UAT|`staging.ng-fields.ngs.tg`|
|`production`|Utilisation opérationnelle|`app.ng-fields.ngs.tg`|

---

## 5. Déploiement

### 5.1 Stratégie de déploiement

Le déploiement suit une méthodologie **Agile Scrum** avec des sprints hebdomadaires. Le pipeline CI/CD est configuré dès le Sprint 1 (S-01) et permet des livraisons continues vers les environnements de staging et de production.

**Principe de déploiement continu :**

```
Commit GitHub
     │
     ▼
GitHub Actions (CI)
  ├── Lint + Tests unitaires
  ├── Tests d'intégration
  ├── Build Docker image
  └── Push vers registry
         │
         ▼
  Déploiement staging (automatique)
         │
         ▼
  Validation manuelle (David KATOH)
         │
         ▼
  Déploiement production (manuel ou automatique)
```

### 5.2 Planning de déploiement par sprint

|Sprint|Période|Objectif de déploiement|
|---|---|---|
|S-01|Semaine 1|Pipeline CI/CD opérationnel, environnements dev/staging/prod, authentification JWT + MFA|
|S-02|Semaine 2|Module clients et gestion des utilisateurs en staging|
|S-03|Semaine 3|Formulaire d'intervention sections 1–4 en staging|
|S-04|Semaine 4|Formulaire complet (sections 5–8) + photos + signatures en staging|
|S-05|Semaine 5|Géolocalisation GPS, calcul durée, intégration offline (SQLite)|
|S-06|Semaine 6|File de synchronisation hors-ligne, résolution conflits|
|S-07|Semaine 7|Génération PDF automatique + envoi email et WhatsApp|
|S-08|Semaine 8|Système de notifications push et email|
|S-09|Semaine 9|Dashboard manager + exports CSV/Excel + intégration OpenProject|
|S-10|Semaine 10|Tests UAT avec techniciens et managers, corrections bugs|
|S-11|Semaine 11|**Mise en production V1.0**, publication App Store et Google Play|

**Jalon de mise en production officielle : 30/06/2026**

### 5.3 Publication mobile

La publication de l'application mobile sur les stores suit les étapes suivantes :

**Google Play Store (Android) :**

- Création d'un compte développeur Google Play (25 $ one-time).
- Signature de l'APK avec un keystore dédié NG-STARs.
- Soumission pour révision (délai estimé : 1–3 jours ouvrés).
- Déploiement en accès restreint (alpha/beta) avant la mise en production.

**Apple App Store (iOS) :**

- Adhésion au programme Apple Developer (99 $/an).
- Signature avec un certificat de distribution valide.
- Soumission pour révision App Review (délai estimé : 1–7 jours ouvrés).
- Déploiement via TestFlight pour les tests UAT avant validation finale.

### 5.4 Containerisation

L'ensemble des services backend est containerisé avec Docker. La configuration Docker Compose pour l'environnement de production inclut les services suivants :

|Service|Image|Port exposé|
|---|---|---|
|`api`|`ng-fields-backend:latest`|3000|
|`postgres`|`supabase/postgres:15`|— (interne)|
|`redis`|`redis:7-alpine`|— (interne)|
|`nginx`|`nginx:alpine`|80, 443|

### 5.5 Plan de reprise d'activité (PRA)

|Scénario|Délai de reprise cible|Procédure|
|---|---|---|
|Panne serveur VPS|≤ 4 heures|Redéploiement depuis l'image Docker la plus récente sur un nouveau VPS|
|Corruption base de données|≤ 2 heures|Restauration depuis la sauvegarde quotidienne Supabase (rétention 30 jours)|
|Indisponibilité OpenProject|Transparente|Les demandes client sont mises en file d'attente et traitées en différé (retry backoff exponentiel, max 24 h)|
|Indisponibilité WhatsApp API|Transparente|Basculement automatique vers l'envoi par email (canal de secours)|

---

## 6. Interopérabilité et systèmes externes

### 6.1 OpenProject API REST v3

OpenProject est le système de gestion de tickets utilisé par NG-STARs. L'intégration est réalisée via l'API REST v3, accessible depuis le backend NG-Fields.

**Endpoints utilisés :**

|Opération|Méthode|Endpoint|
|---|---|---|
|Créer un ticket|POST|`/api/v3/work_packages`|
|Mettre à jour un ticket|PATCH|`/api/v3/work_packages/{id}`|
|Consulter un ticket|GET|`/api/v3/work_packages/{id}`|
|Lister les projets|GET|`/api/v3/projects`|

**Corps de la requête de création (exemple) :**

```json
{
  "subject": "Demande d'intervention — [Nom Client] — [Type]",
  "description": { "raw": "Description saisie par le client" },
  "priority": { "href": "/api/v3/priorities/3" },
  "status": { "href": "/api/v3/statuses/1" },
  "_links": {
    "project": { "href": "/api/v3/projects/{project_id}" },
    "type": { "href": "/api/v3/types/1" }
  }
}
```

**Authentification :** API Token Bearer (configuré par l'administrateur IT dans les variables d'environnement du backend).

**Gestion des erreurs :** En cas d'indisponibilité d'OpenProject, les demandes sont placées dans une file Redis (Bull queue) avec une stratégie de retry à backoff exponentiel. Le délai maximum de retry est de 24 heures avant notification de l'administrateur.

### 6.2 Service Email — SMTP / SendGrid

L'envoi des rapports PDF par email et des notifications est délégué à un service SMTP externe.

|Paramètre|Valeur|
|---|---|
|Fournisseur recommandé|SendGrid (tier gratuit : 100 emails/jour) ou SMTP NG-STARs|
|Protocole|SMTP avec TLS (port 587)|
|Authentification|API Key (variable d'environnement `SMTP_API_KEY`)|
|Délai d'envoi cible|≤ 2 minutes|
|Mécanisme de relance|Bull queue avec retry automatique (backoff exponentiel)|

**Contenu type d'un email de rapport :**

- Objet : `Rapport d'intervention — [Client] — [Date] — Réf. [ID]`
- Corps : résumé de l'intervention (technicien, durée, résultat)
- Pièce jointe : rapport PDF complet

### 6.3 WhatsApp Business API

L'envoi du rapport via WhatsApp est réalisé via l'API WhatsApp Business Cloud (Meta).

|Paramètre|Valeur|
|---|---|
|Fournisseur|Meta WhatsApp Business Cloud API|
|Authentification|Bearer Token (variable d'environnement `WHATSAPP_TOKEN`)|
|Format du message|Lien sécurisé hébergé vers le PDF (URL signée, expiration 72 h)|
|Numéro d'envoi|Numéro NG-STARs enregistré sur la plateforme Meta|
|Canal de secours|En cas d'indisponibilité → basculement automatique vers email|

### 6.4 Service de notifications push — FCM / APNs

Les notifications push en temps réel sont délivrées via :

- **Firebase Cloud Messaging (FCM)** pour Android
- **Apple Push Notification service (APNs)** pour iOS

|Paramètre|Valeur|
|---|---|
|SDK serveur|Firebase Admin SDK (Node.js)|
|Authentification FCM|Service Account JSON (variable d'environnement)|
|Authentification APNs|Certificat `.p12` ou clé `.p8`|
|Délai de livraison cible|≤ 1 minute|
|Mécanisme de secours|Si push échoue → envoi de la notification par email|

**Événements déclencheurs de notifications push :**

|Événement|Destinataire|
|---|---|
|Création d'une nouvelle intervention|Manager|
|Clôture d'une intervention|Manager|
|Dépassement du seuil de durée paramétré|Manager|
|Nouvelle demande client (ticket OpenProject créé)|Manager|
|Intervention planifiée assignée|Technicien concerné|
|Rapport PDF prêt|Technicien + Manager|

### 6.5 Stockage des objets (photos et PDFs)

|Paramètre|Valeur|
|---|---|
|Solution retenue|Supabase Storage (compatible S3)|
|Formats acceptés|JPEG / PNG (photos), PDF (rapports)|
|Taille maximale par photo|500 Ko après compression|
|Accès|URLs signées à durée limitée (72 h pour partage WhatsApp)|
|Chiffrement au repos|Activé côté Supabase Storage|

---

## 7. Plan de supervision et de journalisation

### 7.1 Objectifs de supervision

La supervision du système NG-Fields vise à garantir la disponibilité opérationnelle, détecter rapidement les anomalies, et permettre l'analyse rétrospective des incidents. Elle couvre les composantes backend, base de données, files de tâches et intégrations externes.

### 7.2 Niveaux de journalisation

|Niveau|Usage|
|---|---|
|`ERROR`|Erreurs critiques bloquant une fonctionnalité (ex. : échec génération PDF, échec création ticket OpenProject)|
|`WARN`|Situations anormales non bloquantes (ex. : retry de synchronisation, basculement canal de secours email)|
|`INFO`|Événements métier significatifs (ex. : création d'intervention, envoi rapport, connexion utilisateur)|
|`DEBUG`|Traces détaillées pour le débogage (désactivé en production)|

### 7.3 Éléments journalisés

Chaque entrée de journal doit contenir les champs suivants :

|Champ|Description|
|---|---|
|`timestamp`|Horodatage ISO 8601 (UTC)|
|`level`|Niveau de log (ERROR / WARN / INFO / DEBUG)|
|`userId`|Identifiant de l'utilisateur (si authentifié)|
|`module`|Module NestJS concerné (ex. : InterventionModule, PdfModule)|
|`action`|Opération réalisée (ex. : `CREATE_INTERVENTION`, `SEND_PDF_EMAIL`)|
|`resourceId`|Identifiant de la ressource concernée|
|`ip`|Adresse IP du client|
|`status`|Statut HTTP ou résultat de l'opération|
|`duration_ms`|Durée d'exécution en millisecondes|
|`message`|Message descriptif de l'événement|

### 7.4 Audit trail

Conformément aux exigences RGPD et de traçabilité, un **journal d'audit horodaté** est maintenu pour toutes les actions utilisateurs sensibles :

|Action auditée|Table concernée|
|---|---|
|Connexion / Déconnexion|`audit_logs`|
|Création / Modification / Suppression d'une intervention|`audit_logs`|
|Apposition d'une signature|`audit_logs`|
|Génération et envoi d'un rapport PDF|`audit_logs`|
|Création / Modification d'un compte utilisateur|`audit_logs`|
|Création d'un ticket OpenProject|`audit_logs`|
|Exportation de données CSV/Excel|`audit_logs`|

### 7.5 Métriques de supervision

Les métriques suivantes sont exposées via un endpoint `/metrics` (format Prometheus) et consultables via un tableau de bord de supervision :

|Métrique|Description|
|---|---|
|`api_response_time_ms`|Temps de réponse moyen de l'API REST|
|`pdf_generation_duration_ms`|Durée de génération des rapports PDF|
|`sync_queue_size`|Nombre de fiches en attente de synchronisation|
|`notification_delivery_rate`|Taux de succès des notifications push|
|`openproject_api_error_rate`|Taux d'erreur des appels à l'API OpenProject|
|`db_connection_pool_usage`|Utilisation du pool de connexions PostgreSQL|
|`redis_queue_length`|Longueur des files Bull (PDF, email, notifications)|

### 7.6 Alertes automatiques

|Seuil d'alerte|Niveau|Canal d'alerte|
|---|---|---|
|Temps de réponse API > 5 s pendant 3 min|WARNING|Email administrateur|
|Génération PDF > 30 s|WARNING|Email administrateur|
|File de synchronisation > 50 fiches en attente|WARNING|Email + notification push administrateur|
|Taux d'erreur API > 5 % sur 10 min|CRITICAL|Email + SMS administrateur|
|Espace disque VPS > 80 %|CRITICAL|Email administrateur|
|Service backend indisponible (healthcheck)|CRITICAL|Email + SMS administrateur|

### 7.7 Healthcheck

Un endpoint de vérification d'état est exposé par le backend :

```
GET /health
```

**Réponse attendue (200 OK) :**

```json
{
  "status": "ok",
  "components": {
    "database": "up",
    "redis": "up",
    "openproject": "up",
    "storage": "up"
  },
  "uptime_seconds": 86400,
  "version": "1.0.0"
}
```

---

## 8. Sécurité système

### 8.1 Authentification et autorisation

**Mécanisme d'authentification :** JSON Web Tokens (JWT)

|Paramètre|Valeur|
|---|---|
|Algorithme de signature|HS256 (ou RS256 selon environnement)|
|Durée de vie du token d'accès|15 minutes|
|Durée de vie du token de rafraîchissement|7 jours|
|Authentification multi-facteurs (MFA)|Obligatoire pour les rôles MANAGER et ADMIN|
|Verrouillage de compte|Après 5 tentatives échouées consécutives|
|Déblocage|Email de réinitialisation envoyé automatiquement|

**Modèle de contrôle d'accès (RBAC) :**

|Rôle|Permissions principales|
|---|---|
|`TECHNICIAN`|Créer/modifier ses propres interventions, consulter ses fiches, photographier, signer|
|`MANAGER`|Toutes les permissions technicien + tableau de bord, notifications, signature différée, exports, affectation|
|`ADMIN`|Toutes les permissions + gestion des comptes, paramétrage des seuils, administration système|

### 8.2 Chiffrement des communications

|Exigence|Implémentation|
|---|---|
|HTTPS obligatoire sur tous les endpoints|TLS 1.3 minimum via certificat Let's Encrypt (renouvellement automatique)|
|Redirections HTTP → HTTPS|Configurées au niveau Nginx|
|Headers de sécurité HTTP|`Strict-Transport-Security`, `X-Content-Type-Options`, `X-Frame-Options`, `Content-Security-Policy`|
|Certificats API externes|Vérification stricte des certificats TLS des services tiers (OpenProject, WhatsApp, SMTP)|

### 8.3 Chiffrement des données au repos

|Données|Mécanisme de chiffrement|
|---|---|
|Base de données locale mobile (SQLite)|SQLCipher (AES-256-CBC)|
|Base de données PostgreSQL|Chiffrement au niveau Supabase + chiffrement des colonnes sensibles (passwords, signatures)|
|Stockage objets (photos, PDFs)|Chiffrement côté serveur activé sur Supabase Storage|
|Variables d'environnement sensibles|Gestion via secrets GitHub Actions et variables d'environnement VPS sécurisées|

### 8.4 Protection contre les attaques courantes

|Vecteur d'attaque|Contre-mesure implémentée|
|---|---|
|Injection SQL|Utilisation exclusive de l'ORM (TypeORM/Prisma) avec requêtes paramétrées|
|Cross-Site Scripting (XSS)|Échappement systématique des sorties HTML, Content-Security-Policy|
|Cross-Site Request Forgery (CSRF)|Tokens CSRF sur les formulaires web sensibles|
|Brute-force sur authentification|Rate limiting (5 tentatives / 15 min par IP via Redis) + verrouillage compte|
|DDoS applicatif|Rate limiting global sur l'API REST (100 req/min par IP)|
|Exposition de données sensibles|Jamais de log des mots de passe, tokens ou données personnelles|
|Injection dans les PDFs|Sanitisation des données avant assemblage (bibliothèque DOMPurify côté client)|

### 8.5 Conformité RGPD

|Exigence RGPD|Implémentation technique|
|---|---|
|Minimisation des données|Seules les données strictement nécessaires sont collectées|
|Consentement documenté|Formulaire d'acceptation lors de la création du compte utilisateur|
|Droit à l'effacement|API d'anonymisation/suppression des données personnelles disponible pour l'ADMIN|
|Registre des traitements|Documentation maintenue par l'ADMIN, accessible depuis le back-office|
|Sauvegardes automatiques|Backup quotidien PostgreSQL, rétention 30 jours, chiffrées|
|Portabilité des données|Export CSV/JSON des données personnelles disponible sur demande|
|Journalisation des accès aux données sensibles|Audit trail complet (section 7.4)|

### 8.6 Gestion des secrets

Aucun secret (clé API, mot de passe, token) ne doit jamais être commis dans le dépôt Git. La gestion des secrets suit les règles suivantes :

- Variables d'environnement stockées dans des fichiers `.env` non versionnés (`.gitignore`).
- En production : secrets injectés via les variables d'environnement du VPS et les secrets GitHub Actions.
- Rotation régulière des clés JWT (au moins tous les 90 jours).
- Audit trimestriel des accès aux ressources cloud (Supabase, VPS).

---

## 9. Diagramme de déploiement

Le diagramme ci-dessous représente la configuration physique et logique de l'infrastructure NG-Fields en production, conformément aux conventions UML 2 du diagramme de déploiement.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          ENVIRONNEMENT PRODUCTION                            │
│                                                                               │
│  ┌─────────────────────────────┐   ┌────────────────────────────────────┐   │
│  │  Appareil Mobile Technicien  │   │     Poste Manager / Directeur      │   │
│  │  (iOS 14+ / Android 10+)     │   │  (navigateur Chrome/Firefox/Edge)  │   │
│  │  ┌────────────────────────┐ │   │  ┌──────────────────────────────┐  │   │
│  │  │  Application NG-Fields  │ │   │  │  Tableau de bord Web          │  │   │
│  │  │  React Native / Flutter │ │   │  │  React + Next.js              │  │   │
│  │  │  ┌──────────────────┐  │ │   │  │  ng-fields.ngs.tg/dashboard  │  │   │
│  │  │  │ SQLite Chiffrée  │  │ │   │  └──────────────────────────────┘  │   │
│  │  │  │ (SQLCipher)      │  │ │   └──────────────┬─────────────────────┘   │
│  │  │  └──────────────────┘  │ │                  │ HTTPS/TLS 1.3            │
│  │  └────────────────────────┘ │                  │                          │
│  └──────────────┬───────────────┘                  │                          │
│                 │ HTTPS/TLS 1.3                     │                          │
│                 │                                   │                          │
│  ┌──────────────▼───────────────────────────────────▼──────────────────────┐ │
│  │                    VPS Hetzner (Ubuntu 24, 2 vCPU, 4 Go RAM)             │ │
│  │                                                                           │ │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │ │
│  │  │  Nginx (Reverse Proxy + TLS Let's Encrypt)                          │  │ │
│  │  │  Ports : 80 (redirect → 443), 443                                   │  │ │
│  │  └──────────────────────────────┬─────────────────────────────────────┘  │ │
│  │                                  │ Port 3000 (interne)                     │ │
│  │  ┌───────────────────────────────▼─────────────────────────────────────┐  │ │
│  │  │  Backend NG-Fields — Node.js + NestJS (Docker container)             │  │ │
│  │  │  ├── AuthModule         ├── InterventionModule                      │  │ │
│  │  │  ├── ClientModule       ├── PdfModule                               │  │ │
│  │  │  ├── NotificationModule ├── SyncModule                              │  │ │
│  │  │  ├── OpenProjectModule  └── StorageModule                           │  │ │
│  │  └──────────────────────────────┬──────────────┬────────────────────────┘  │ │
│  │                                  │              │                            │ │
│  │  ┌───────────────────────────────▼───┐  ┌───────▼───────────────────────┐  │ │
│  │  │  Redis (Docker container)          │  │  Supabase Storage              │  │ │
│  │  │  ├── Cache API                    │  │  (Photos + PDFs chiffrés)      │  │ │
│  │  │  └── Bull Queues                  │  └───────────────────────────────┘  │ │
│  │  │    ├── pdf_queue                  │                                      │ │
│  │  │    ├── email_queue                │                                      │ │
│  │  │    ├── push_queue                 │                                      │ │
│  │  │    └── sync_queue                 │                                      │ │
│  │  └───────────────────────────────────┘                                      │ │
│  └──────────────────────────────────────────────────────────────────────────┘ │
│                                                                                │
└────────────────────────────────────────────────────────────────────────────────┘
         │                    │                    │                    │
         ▼                    ▼                    ▼                    ▼
┌────────────────┐  ┌──────────────────┐  ┌──────────────┐  ┌──────────────────┐
│ Supabase       │  │ OpenProject      │  │ FCM / APNs   │  │ WhatsApp Business│
│ PostgreSQL     │  │ API REST v3      │  │ Notifications│  │ API (Meta)       │
│ (Cloud)        │  │ (NG-STARs)       │  │ Push         │  │                  │
│ ├── SSL        │  │ API Token Auth   │  │ ├── Android  │  │ Bearer Token     │
│ └── Backup 30j │  │                  │  │ └── iOS      │  │                  │
└────────────────┘  └──────────────────┘  └──────────────┘  └──────────────────┘

         │
         ▼
┌────────────────┐
│ Service Email  │
│ SendGrid /     │
│ SMTP NG-STARs  │
│ TLS Port 587   │
└────────────────┘
```

**Légende des connexions :**

|Connexion|Protocole|Sécurisation|
|---|---|---|
|Client mobile → Nginx|HTTPS|TLS 1.3, certificat Let's Encrypt|
|Navigateur web → Nginx|HTTPS|TLS 1.3, certificat Let's Encrypt|
|Nginx → Backend NestJS|HTTP|Interne Docker réseau privé|
|Backend → Supabase PostgreSQL|SSL/TCP|Certificat Supabase, pool TypeORM|
|Backend → Redis|TCP|Interne Docker réseau privé|
|Backend → Supabase Storage|HTTPS|API Key Supabase|
|Backend → OpenProject|HTTPS|Bearer Token API v3|
|Backend → FCM / APNs|HTTPS|Service Account / Certificat .p8|
|Backend → WhatsApp API|HTTPS|Bearer Token Meta|
|Backend → Service Email|SMTP TLS|API Key SendGrid / Credentials SMTP|

---

## 10. Conclusion

Ce document de capture des besoins techniques définit le cadre technique complet du projet NG-Fields. Il précise les choix d'architecture, les technologies retenues, les contraintes de déploiement, les mécanismes de sécurité et les modalités de supervision qui encadreront l'ensemble du cycle de développement.

Les principaux points à retenir sont les suivants :

**Architecture offline-first non négociable.** La réalité terrain de NG-STARs — avec 20 % des interventions en zone à connectivité nulle — impose une conception mobile fondée sur la persistance locale chiffrée (SQLite + SQLCipher) et la synchronisation automatique différée, avec résolution de conflits intégrée.

**Stack technique ouverte et économique.** L'ensemble des technologies retenues (Node.js + NestJS, React Native ou Flutter, PostgreSQL via Supabase, Redis) est open-source, maîtrisé par l'équipe senior NG-STARs, et compatible avec le budget mensuel disponible de 12 000 XOF, avec un coût d'hébergement estimé entre 25 et 35 €/mois.

**Sécurité dès la conception.** La conformité RGPD, le chiffrement TLS 1.3 sur toutes les communications, le chiffrement au repos des données sensibles, l'authentification JWT + MFA et l'audit trail complet sont traités comme des exigences fondamentales et non comme des options.

**Intégration maîtrisée des systèmes externes.** Les quatre intégrations critiques (OpenProject, Email, WhatsApp, Push) disposent chacune d'un mécanisme de relance automatique (Bull queues) et, le cas échéant, d'un canal de secours, garantissant la continuité de service même en cas d'indisponibilité partielle d'un service tiers.

Ce document constitue la référence technique du projet à compter de sa date de validation officielle et devra être mis à jour à chaque évolution significative de l'architecture ou des choix technologiques.

---

|Auteur du Document|Validateur Technique|Validateur Final|
|---|---|---|
|Nelson Emmanuel FOLLY|Barnabé MIDJRATO|David KATOH|
|Signature : ___________|Signature : ___________|Signature : ___________|
|Date : _______________|Date : _______________|Date : _______________|
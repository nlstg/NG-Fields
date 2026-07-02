
> **Note :** Cette critique se réfère à l'ancienne version du Cahier des Charges (V1, avril 2026).  
> Le nouveau Cahier des Charges (V2, 18 mai 2026) a intégré les corrections listées ci-dessous.  
> Ce document est conservé pour trace mais **ne s'applique plus** à la version actuelle du projet.

---

## Critique Experte — Cahier des Charges NG-Fields V1

### Appréciation Générale

Le document présente une structure solide et un niveau de détail . Cependant, en tant que cahier des charges destiné à servir de **référence contractuelle**, plusieurs lacunes sérieuses compromettent sa valeur opérationnelle réelle. Voici les points à corriger avant toute validation.

---

### 1. Générale

**Ce qui est problématique** — Le document écrit comme si l'option de développement est choisie alors que se derniers n'est même pas encore choisie aborder donc ne parle pas de projets ng-fields par plus-tôt d'un projets de **DIGITALISATION DE LA GESTION DES INTERVENSIONS** la présentations du projets ng-fields doit être enlever dans la presentation et de meme que le périmètre donc 1.3 et 1.4 doit disparaitre

---

### 2. Introduction

**Ce qui va bien** — Le ton est positif et orienté ambition, comme demandé. Les trois paragraphes couvrent le contexte, la solution et la portée du document.

**Ce qui est problématique** — L'introduction ne cite toujours pas explicitement la **date de rédaction, la version du document, ni le public cible** du CDC. Un lecteur qui ouvre ce document sans la page de garde ne sait pas immédiatement à qui il s'adresse (MOA ? Prestataire ? Équipe interne ?). Il manque également une phrase précisant que le document a été rédigé dans le cadre d'un **développement interne**, ce qui est pourtant le fait le plus structurant du projet.

---

### 3. Section 1 — Présentation du Projet

**Ce qui va bien** — Le périmètre V1/hors-V1 est clairement délimité, ce qui est une très bonne pratique. La distinction entre ce qui est inclus et ce qui est reporté en V2 protège l'équipe contre le scope creep.

**Problèmes identifiés :**

Premièrement, la **section 1.2 sur NG-Fields** présente l'application en listant des fonctionnalités sans expliquer la **valeur métier** qu'elle apporte. Un CDC doit répondre à la question « pourquoi » avant de répondre à la question « quoi ». La phrase de présentation devrait articuler le bénéfice attendu pour NG-STARs avant d'entrer dans les détails techniques.

Deuxièmement, le **tableau de l'équipe technique (section 1.5)** comporte quatre lignes avec « À compléter » dans la colonne Nom. C'est acceptable en brouillon, mais un document soumis à validation ne peut pas contenir des données manquantes aussi fondamentales. Sans noms, les responsabilités ne sont pas opposables. Il faudra compléter ce tableau avant la signature.

Troisièmement, la **colonne Responsabilité du Directeur** indique simplement « Décision stratégique et budgétaire » sans que son nom soit renseigné. Cela fragilise juridiquement la table des signataires en conclusion.

---

### 4. Section 2 — Problématique


**Problèmes identifiés :**

LA problematique n'a pas respecter l'ordre naturel de la presnetation d'une problematique enleve les phase et decompose la probleme en 3 paragraphe premier l'etat des lieu tu resume les phase en 1 seul paragraphe ,le deuxieme paragraphe doit contenir les problemes reperer et la troisieme doit etre la partie des question minimum 3 tu

La **section 2.3 sur les enjeux** je me demande ou je peux le deplace je me demande si ses favorable de le laisser dans la problematique  liste cinq enjeux sous forme de bullets mais ne les hiérarchise pas. Or dans un CDC, les enjeux doivent être classés par ordre de priorité ou d'urgence pour orienter les arbitrages. Si deux fonctionnalités entrent en conflit, l'équipe de développement n'a aucune indication sur ce qui prime entre, par exemple, la conformité RGPD et la réactivité managériale.

Par ailleurs, les **impacts dans le tableau 2.2** sont trop qualitatifs. Un CDC sérieux devrait quantifier certains impacts quand c'est possible. Par exemple pour le problème n°1 (perte de temps pour récupérer la fiche), on pourrait indiquer « estimation : 15 minutes perdues par intervention, soit ~2h30/semaine pour 10 interventions ». Sans chiffres, les impacts restent des opinions. 


---

### 5. Section 3 — Objectifs et Résultats Attendus


**Problèmes identifiés :**

Probleme generale separe objectifs et Resultat attendus en 2 grand partie ne les fusionne

L'**objectif général (section 3.1)** est une longue phrase de 55 mots qui accumule quatre objectifs distincts. Un objectif général dans un CDC doit être une phrase simple, mémorisable et non ambiguë. Tout ce qui est secondaire doit descendre dans les objectifs spécifiques. et aussi l'objectifs principales  que le responsable IT ma  est de savoir le **temps precis**  passer sur site 

Et aussi transforme le tableaux en liste de puce bien écrit dans un Français facile a comprendre

La **section 3.4 sur les résultats attendus** liste des livrables mais sans aucune indication de **critère d'acceptation**. Un livrable n'est pas validable si l'on ne sait pas à quel standard il doit répondre. Par exemple, « Application mobile fonctionnelle » : que signifie « fonctionnelle » ? Aucun bug bloquant ? Testé sur quels appareils ? Avec quel taux de couverture de tests ? Ces précisions sont indispensables pour les phases UAT et de recette.

---

### 6. Section 4 — Étude et Critique de l'Existant

**Ce qui va bien** — L'analyse comparative des solutions du marché est honnête et bien argumentée. La justification du développement sur mesure est solide et convaincante.

**Problèmes identifiés :**

Le **titre « Étude et Critique de l'Existant »**  Cette partie aussi doit etre scinder en 2 grand partie Etude puis Critique 

LEs points fort doivent etre long mais ne doit pas atteindre le nivaux de la critique doit faire une page minimum 

La **section 4.2** compare des solutions comme Synchroteam ou ServiceMax sans indiquer leurs **tarifs réels**. La comparaison reste donc superficielle. Un lecteur décideur ne peut pas vérifier que ces solutions sont effectivement hors budget sans un chiffre. Il suffirait d'ajouter une colonne « Tarif indicatif » avec des ordres de grandeur (ex. : « à partir de 50 €/utilisateur/mois »).

SAche que l'option de developpement n'est pas chosiie car je dois proposer tout les aspect de chaque solution donc les developperea ne les fait pas dans un tableaux

---

### 7. Section 5 — Proposition et Choix de Solution

**Ce qui va bien** — Le formulaire en 8 sections est le cœur du document et il est très bien traité. Le niveau de détail est suffisant pour qu'un développeur puisse commencer à travailler.

**Problèmes majeurs identifiés :**

Auncune n'option de choix n'esgt prise donc alors que dans cette on dirait qu'il a ete deja fait 

C'est la section la plus incomplète sur le plan fonctionnel. Il manque :

Un **diagramme ou schéma des flux applicatifs**. Pour un CDC destiné à des développeurs senior, l'absence de schéma de navigation entre les écrans (même simplifié) est un manque sérieux. On ne sait pas, par exemple, si le formulaire multi-étapes permet de revenir en arrière, si une intervention peut être sauvegardée en brouillon, ni quel est le flux exact entre la signature et la génération du PDF.

Des **règles métier explicites** pour les cas d'usage ambigus. Par exemple : que se passe-t-il si le technicien oublie de saisir l'heure de sortie ? Peut-il soumettre le formulaire ? Peut-il signer à la place du responsable ? Qu'arrive-t-il à une intervention commencée hors ligne si le technicien change de téléphone ? Ces règles sont essentielles pour éviter les ambiguïtés en cours de développement.

Des **contraintes de validation des champs** précises. Le tableau indique « Obligatoire : Oui » mais ne précise pas les règles de format. Quel format pour le numéro de téléphone ? Quelle longueur maximale pour les champs texte long ? Ces précisions évitent des allers-retours coûteux lors des recettes.

La **gestion des droits d'accès** n'est décrite nulle part dans cette section. Qui peut modifier une intervention après validation ? Un manager peut-il éditer ce qu'un technicien a saisi ? L'absence d'une matrice CRUD (Create / Read / Update / Delete) par profil est une lacune critique dans tout CDC applicatif.

---

### 8. Section 6 — Étude des Technologies

**Ce qui va bien** — L'honnêteté de l'approche est appréciable : on reconnaît que les choix ne sont pas encore arrêtés, ce qui est plus professionnel que d'indiquer un stack fictif. Les critères de sélection sont bien posés.

**Problèmes identifiés :**

Les **recommandations préliminaires** sont rédigées en italique comme des notes de bas de page, alors qu'elles devraient être des conclusions claires encadrées dans des cellules dédiées ou des paragraphes distincts. Dans l'état actuel, leur statut est ambigu : sont-elles des décisions ou des suggestions ?

La section **6.6 sur la sécurité** mentionne le RGPD mais sans préciser quelle **base légale** justifie le traitement des données des techniciens et des clients (consentement, intérêt légitime, obligation contractuelle). C'est une exigence minimale pour toute application traitant des données personnelles dans l'espace francophone.

Il n'y a **aucune mention des APIs tierces** qui seront nécessaires : API WhatsApp Business, service d'envoi d'emails (SMTP ou service tiers comme SendGrid), service de génération de PDF côté serveur, service de stockage des médias (S3-compatible). Ces dépendances externes ont des implications sur les coûts, les délais et la maintenance que le CDC doit anticiper.

---

### 9. Section 7 — Planning Prévisionnel

**Ce qui va bien (V1)** — Le découpage en 11 sprints était réaliste et le niveau de détail par sprint était suffisant pour démarrer. La mise en évidence du jalon de mise en production était claire.

**Problèmes sérieux identifiés :**

Le **planning ne comporte aucune marge de sécurité**. Le dernier sprint (S-11, 28-30 juin) est composé uniquement de 3 jours, censés couvrir le déploiement en production, la publication sur les deux stores (App Store et Google Play) et la rédaction de toute la documentation. C'est irréaliste à deux niveaux : la soumission sur l'App Store d'Apple peut prendre de 1 à 7 jours ouvrés selon les délais de revue, et la documentation d'une application de cette envergure nécessite plusieurs jours de travail. Ce sprint doit être revu.

Il **manque un diagramme de Gantt** ou au minimum une représentation visuelle de l'enchaînement des sprints et de leurs dépendances. Un CDC avec planning doit permettre d'identifier les chemins critiques.

La **phase UAT (Sprint 10)** est d'une seule semaine pour un formulaire de 8 sections impliquant 15 techniciens, 10 managers et 10 administrateurs. Ce délai est clairement insuffisant pour réaliser des tests d'acceptance sérieux avec des utilisateurs réels sur le terrain. Une semaine supplémentaire devrait être intégrée, quitte à réviser la date de mise en production.

Le **budget (section 7.4)** comporte une ligne « Abonnement Claude Pro : 20 €/mois » listée comme coût initial ET récurrent. Cela peut prêter à confusion pour un lecteur décideur qui ne comprend pas le rôle de cet outil dans le projet. Il serait plus clair de renommer cette ligne « Assistance IA au développement (Claude Pro) » et d'ajouter une courte note explicative.

---

### 10. Conclusion

**Ce qui va bien** — La conclusion est bien rédigée, synthétique et reprend efficacement les bénéfices attendus. Le tableau de signatures est professionnel.

**Ce qui manque** — Il n'y a **aucune clause de révision** du document. Un CDC contractuel doit préciser explicitement selon quelle procédure il peut être amendé (qui peut demander une modification, qui doit valider, quel délai de réponse). Sans cette clause, le document ne protège pas réellement les deux parties en cas de désaccord sur le périmètre.

---

### Synthèse des Priorités de Correction

Les points suivants doivent impérativement être traités avant la validation officielle du document :

**Priorité haute** — Ajouter un tableau d'historique des révisions, compléter les noms de l'équipe et du directeur, ajouter une matrice CRUD des droits d'accès par profil, intégrer les règles de validation des champs du formulaire, revoir le Sprint 11 (irréaliste), ajouter les délais de revue App Store dans le planning.

**Priorité moyenne** — Quantifier les impacts dans le tableau 2.2, ajouter un schéma de navigation applicative, lister les APIs tierces nécessaires, préciser les critères d'acceptation des livrables (section 3.4), ajouter les tarifs dans le tableau comparatif des solutions.

**Priorité basse** — Renommer le titre de la section 4, hiérarchiser les enjeux stratégiques, clarifier le statut des recommandations technologiques, ajouter une clause de révision en conclusion.

---

Souhaitez-vous que j'intègre directement toutes ces corrections dans une V2 du document Word ?
# Processus d'intervention terrain

## Déroulement

1. **Déclenchement** — Appel client / email / ticket OpenProject → création intervention
2. **Planification** — Manager affecte un technicien (via dashboard web)
3. **Préparation** — Technicien voit la mission sur son app mobile
4. **Déplacement** — GPS départ société →导航 client
5. **Arrivée** — GPS arrivée client, début intervention
6. **Diagnostic** — Description du problème, équipement concerné
7. **Travaux** — Réparation / maintenance / installation
8. **Photos** — Avant/après (optionnel)
9. **Facturation** — Consommables, pièces, temps passé
10. **Signatures** — Client + Technicien + Responsable (si nécessaire)
11. **Fin** — GPS fin intervention, retour société
12. **Validation** — Manager vérifie et valide
13. **Transmission** — PDF généré → email client + WhatsApp
14. **Clôture** — Ticket OpenProject mis à jour

## Rôles

| Rôle | Actions |
|------|---------|
| Admin | Configuration, utilisateurs, audit |
| Manager | Planification, validation, dashboard, export |
| Technicien | Réalisation terrain, formulaire, photos, signature |
| Client | Réception du rapport PDF, suivi portail (à venir) |

## Statuts d'intervention

`PENDING` → `IN_PROGRESS` → `COMPLETED` → `VALIDATED` → `BILLED`
                ↕
             `CANCELLED`

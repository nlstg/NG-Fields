# Intégration Twilio WhatsApp

## Objectif
Envoyer le rapport PDF d'intervention par WhatsApp au client.

## Configuration

### Compte Twilio
1. Créer un compte Twilio
2. Activer WhatsApp Sandbox (ou numéro dédié)
3. Récupérer Account SID + Auth Token

### Variables d'environnement
```
TWILIO_ACCOUNT_SID=votre_sid
TWILIO_AUTH_TOKEN=votre_token
TWILIO_WHATSAPP_FROM=+14155238886  # Numéro sandbox Twilio
```

### API utilisée
- `POST /api/notifications/whatsapp` — Envoi du PDF via Twilio API
- Le PDF est d'abord uploadé sur Supabase Storage
- Twilio envoie un message média avec le lien de téléchargement

## Format du message
```
NG-STARs — Rapport d'intervention
Réf: XXXXXXXX
Client: [Nom]
Date: [Date]

Votre rapport est disponible : [lien PDF]
```

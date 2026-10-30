# TripTrace 🌍

Application Android universelle de suivi de voyages.

Le projet est conçu pour fonctionner avec n'importe quel pays et plusieurs modes de transport.

## Objectif

Créer un voyage, l'activer, puis enregistrer automatiquement la trace GPS même lorsque l'application est en arrière-plan.

## Stack

- Kotlin
- Jetpack Compose
- Room / SQLite
- Google Fused Location Provider
- MapLibre (préparé pour la carte)
- Architecture local-first

## Fonctionnalités V0.2

- Plusieurs voyages
- Nom du voyage
- Moyen de transport
- Devise
- Voyage actif
- GPS en arrière-plan
- Stockage local des positions
- Latitude / longitude / altitude
- vitesse / précision / timestamp
- architecture prête pour la carte mondiale

## Roadmap

### V0.3 — Carte
- MapLibre
- position actuelle
- tracé réel
- distance
- vitesse moyenne
- vitesse maximale

### V0.4 — Itinéraire
- étapes
- itinéraire prévu
- itinéraire réel
- import/export GPX
- GeoJSON

### V0.5 — Carnet
- photos
- journal
- POI
- hébergements

### V0.6 — Voyage
- carburant
- autonomie
- dépenses
- multi-devises
- statistiques

### V0.7 — Offline
- cartes hors ligne
- synchronisation différée

### V1.0
- compte utilisateur
- cloud
- partage familial
- tableau de bord web

## GitHub

```bash
git init
git add .
git commit -m "Initial TripTrace universal travel tracker"
git branch -M main
git remote add origin https://github.com/<USERNAME>/triptrace.git
git push -u origin main
```

## Important

Le projet est un squelette fonctionnel destiné au développement. Avant publication Play Store, il faudra notamment compléter la gestion des permissions en arrière-plan, la politique de confidentialité, les tests sur différentes versions Android et la configuration d'un fournisseur de cartes/tuiles.

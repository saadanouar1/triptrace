# TripTrace Architecture

## Vision

TripTrace est une application **universal travel tracker**.

Le Canada n'est qu'un voyage parmi d'autres.

## Modèle

```text
Trip
│
├── Route
├── Days
│   ├── Track
│   │   └── LocationPoint
│   ├── Stop
│   ├── POI
│   ├── Expense
│   ├── FuelEntry
│   ├── Accommodation
│   ├── Photo
│   └── JournalEntry
│
└── Settings
    ├── Currency
    ├── Units
    └── TravelMode
```

## Local-first

```text
GPS
 ↓
Foreground Service
 ↓
Room
 ↓
UI

Internet disponible ?
       │
       ├── Non → continuer localement
       │
       └── Oui → Sync Engine → API → PostgreSQL
```

## Principes

1. Aucun pays codé en dur.
2. Les coordonnées sont toujours stockées en WGS84.
3. La devise originale d'une dépense est conservée.
4. Les unités d'affichage sont configurables.
5. Les données GPS doivent rester utilisables hors ligne.
6. La synchronisation cloud ne doit jamais bloquer le tracking.

## Évolution

Le projet peut ensuite être séparé en modules :

```text
:app
:core
:tracking
:maps
:data
:domain
:feature-trip
:feature-journal
:feature-expenses
:feature-settings
```

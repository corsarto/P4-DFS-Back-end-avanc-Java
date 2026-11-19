# EcoReno Starter

Starter pédagogique pour une application Java/Spring Boot dédiée à la gestion de projets de rénovation écologique.

## Objectifs pédagogiques

- Construire une API REST et une logique métier simple
- Travailler l’accessibilité, la responsivité et la clarté de l’interface
- Pratiquer les bonnes pratiques de qualité de code
- Utiliser l’IA comme partenaire de développement de manière responsable
- Se préparer à la mission certificative du P4 sans remplacer celle-ci

## Prérequis

- Java 17+
- Maven 3.9+

## Démarrage

```bash
mvn spring-boot:run
```

L’API sera disponible sur http://localhost:8080/api/projects.
Le projet frontend sur http://localhost:8080.

## Fonctionnalités de base

- Liste des projets
- Création d’un nouveau projet
- Consultation d’un projet par identifiant
- Mise à jour d’un projet
- Statut et description associés au projet
- Interface web simple et responsive pour pratiquer le front-end

## Structure du projet

- src/main/java/com/ecoreno/controller : contrôleurs REST
- src/main/java/com/ecoreno/model : modèles et enum de statut
- src/main/java/com/ecoreno/service : logique métier
- src/main/java/com/ecoreno/exception : exceptions métier
- src/test/java : tests de base

## API

- GET - /api/projects - Succès: 200 
- GET - /api/projects/{id} - Succès: 200 - Erreurs: 404
- POST - /api/projects - Succès: 201 - Erreurs: 400
- PUT - /api/projects/{id} - Succès: 200 - Erreurs: 400, 404

Format des erreurs:

- 400: { "nomDuChamp": "message" }
- 404: { "error": "message" }

## Choix techniques

- Formulaire en `novalidate` : les messages d'erreur viennent uniquement de l'API (source unique, affichage accessible).


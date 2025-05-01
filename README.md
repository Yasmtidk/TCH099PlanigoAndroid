# PLANIGO 🍳: Organisez, cuisinez, économisez!

Vous cherchez à simplifier la planification de vos repas ? Découvrez PLANIGO, une application conçue pour vous aider à organiser vos repas de la semaine efficacement.
Accédez à une large sélection de recettes ou ajoutez les vôtres, puis créez facilement votre menu hebdomadaire.
PLANIGO calcule automatiquement votre liste de courses, en tenant compte des ingrédients nécessaires et de ceux que vous avez déjà, ce qui vous permet d'économiser du temps et de réduire le gaspillage.
En résumé, PLANIGO est un outil pratique pour mieux gérer vos repas, gagner du temps et faire des économies. Simplifiez-vous la vie avec PLANIGO !

---

## Contributions

Ce projet a été développé par l'équipe C:

*   Yasmine Beddouch
*   Simon Bégin
*   Yanis Chabane
*   Jonathan Chartier
*   Myriam Kechad

---

## Aperçu du Projet

PLANIGO est une application duale (Web et Mobile) conçue pour optimiser la planification de repas, réduire le gaspillage alimentaire et gérer les dépenses culinaires. Elle s'adresse aux individus et aux familles. Ce dépôt contient le code source de l'application **Mobile** pour Android. L'application mobile communique avec le backend/API développé dans le [dépôt Web principal](https://github.com/yan32323/H2025_TCH099_02_C1.git).

Fonctionnalités clés (sur mobile) :

*   **Gestion des recettes:** Rechercher et consulter des recettes. Ajouter, modifier, supprimer (via l'API backend).
*   **Planification hebdomadaire:** Créer, modifier et supprimer des plans de repas (via l'API backend).
*   **Suivi du stock personnel:** Gérer un inventaire des ingrédients disponibles à la maison (via l'API backend).
*   **Liste de courses intelligente:** Générer automatiquement une liste d'achats basée sur le plan de repas et le stock personnel (via l'API backend).
*   **Fonctionnalités communautaires:** Suivre d'autres utilisateurs, commenter, aimer et noter les recettes (via l'API backend).
*   **Gestion de profil:** Gérer ses informations (via l'API backend).
*   **Sécurité:** Inscription, Connexion, Déconnexion (via l'API backend).

---


## Installation

Pour installer et exécuter PLANIGO, suivez les étapes ci-dessous pour les applications Web et Mobile.

### Application Mobile

1.  **Cloner le dépôt:**
    ```bash
    git clone [https://github.com/Yasmtidk/TCH099PlanigoAndroid.git]
    ```
2.  **Ouvrir dans Android Studio:**
    *   Lancez Android Studio.
    *   Sélectionnez "Open an Existing Project" (Ouvrir un projet existant) et naviguez jusqu'au répertoire `mobile/` du dépôt cloné.
3.  **Configuration de l'API Backend:**
    *   Assurez-vous que l'application mobile est configurée pour communiquer avec l'API backend que vous avez déployée à l'étape précédente (généralement dans un fichier de configuration ou des constantes définissant l'URL de l'API).
4.  **Compiler et Exécuter:**
    *   Connectez un appareil Android à votre ordinateur ou lancez un émulateur Android.
    *   Dans Android Studio, cliquez sur le bouton "Run" (Exécuter) pour compiler et déployer l'application sur l'appareil ou l'émulateur sélectionné.

---

## Technologies

*   **Backend (API & Web):** PHP, MySQL
*   **Frontend (Web):** HTML, CSS, JavaScript
*   **Mobile:** Java (Android Studio), XML (Layouts)
*   **Modélisation:** UML (Diagrammes de Classe, Séquence), PlantUML
*   **Design/Prototypage:** Figma
*   **Outils:** GitHub, OCR, AI Studio (pour exploration)

---

## Sprints de Développement

Le projet a suivi une méthodologie Scrum, structurée en 4 sprints principaux pour construire l'application de manière itérative :

*   **Sprint 1 (01/03 - 14/03/2025):** Mise en place de l'environnement de développement, base de données initiale, implémentation des fonctionnalités d'authentification (Inscription, Connexion, Déconnexion) et de gestion de recettes (Ajout, Modification, Suppression) pour l'application Web. Création des premiers wireframes.
*   **Sprint 2 (15/03 - 28/03/2025):** Ajout des fonctionnalités de recherche et consultation de recettes, gestion des plans de repas (Création, Modification, Suppression), gestion du stock d'ingrédients (Web). Début du développement mobile avec l'implémentation de l'authentification et la structuration des interfaces. Développement de l'architecture API.
*   **Sprint 3 (29/03 - 11/04/2025):** Poursuite du développement mobile pour aligner les fonctionnalités avec la version Web (consultation de contenu, gestion du stock, liste de courses mobile). Intégration partielle de la consultation des promotions. Améliorations de navigation et de l'intégration des données dynamiques.
*   **Sprint 4 (12/04 - 25/04/2025):** Intégration des fonctionnalités sociales (Commentaires, Likes, Notation, Suivi d'utilisateurs). Outils d'organisation (Création de listes de plans de repas). Implémentation de la génération de liste de courses sur le Web et restriction d'accès pour les utilisateurs non connectés. Finalisation de l'intégration Web/Mobile et aspects de sécurité essentiels.

---

## Diagrammes

Consultez les diagrammes pour une meilleure compréhension de la structure et des interactions du système:

*   **Diagramme des Cas d'Utilisation:**
    ![Diagramme des Cas d'Utilisation](https://github.com/user-attachments/assets/0d341761-e468-4c91-9da3-20ca307ce53b)

*   **Diagramme d'Architecture:**
    ![Diagramme d'Architecture](https://github.com/user-attachments/assets/0dae0432-fd48-4c76-9439-2377550d5c2f)

*   **Diagramme de Classe:**
    ![image](https://github.com/user-attachments/assets/8eb4ed8d-d6ec-488a-81ff-e82881eb03a9)

*   **Diagrammes de Séquences (Résumé):** Détaille les interactions pour les cas d'utilisation clés. 
    ![image](https://github.com/user-attachments/assets/02a8799e-df6e-4cc4-aa56-687f3b5fffe2)

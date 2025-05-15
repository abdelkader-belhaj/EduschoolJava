# EduschoolJava
# 🎓 Application Éducative Inclusive pour Enfants Non Verbaux

## 🧠 Contexte

Cette application a été conçue pour rendre l'éducation **plus accessible** aux **enfants non verbaux** et pour soutenir leur développement cognitif, émotionnel et social. Elle s’inscrit dans une logique d’**apprentissage tout au long de la vie** et d’**inclusion**, en intégrant des outils adaptés à leurs besoins spécifiques.

## 🚀 Fonctionnalités principales

- 📘 **Gestion des leçons** : création, modification et consultation de leçons adaptées
- 📝 **Gestion des devoirs** : suivi des devoirs à faire et rendus
- 🧩 **Gestion des activités** : organisation d'activités éducatives, ludiques ou thérapeutiques
- 💳 **Gestion des abonnements** : système de paiement en ligne pour l'accès aux services
- 🧑‍⚕️ **Suivi psychologique** : espace sécurisé pour les psychologues pour suivre l’évolution de chaque enfant

## ⚙️ Technologies utilisées

- 🎨 **Frontend (Desktop)** : JavaFX (Java)
- 🧠 **Backend (API REST)** : Symfony (PHP)
- 🗃️ **Base de données** : MySQL  (PHPmyAdmin°
- 💳 **Paiement en ligne** : Intégration d'une passerelle (ex. Stripe, PayPal)
- 🔐 **Authentification** : Gestion des rôles (admin, parent, psychologue, enfant)

## 📦 Installation

### (Symfony)

1. Cloner le dépôt :
   ```bash
   git clone https://github.com/votre-utilisateur/backend-application.git
   cd backend-application
   
2. Installer les dépendances :
composer install

3. Configurer le fichier .env pour la base de données.

4. Créer la base de données :
 ```bash

php bin/console doctrine:database:create
php bin/console doctrine:migrations:migrate
Lancer le serveur :

 ```bash
symfony server:start

### (JavaFX)
1. Ouvrir le projet JavaFX dans votre IDE (IntelliJ / Eclipse)

2. Exécuter le projet JavaFX

📚 Utilisateurs cibles

👨‍👩‍👧‍👦 Parents d'enfants non verbaux

🧑‍🏫 Enseignants spécialisés

🧠 Psychologues

👶 Enfants en situation de handicap

💡 Objectifs
Rendre l’apprentissage accessible aux enfants non verbaux

Offrir un suivi éducatif et psychologique personnalisé

Impliquer les familles et les professionnels dans le processus d’apprentissage

Favoriser l’inclusion par le numérique

🤝 Contribuer
Les contributions sont les bienvenues ! N’hésitez pas à :

Créer une issue pour signaler un bug ou proposer une amélioration

Faire une pull request pour ajouter des fonctionnalités

📄 Licence
Projet open source sous licence MIT.

🧩 Parce que chaque enfant mérite de pouvoir apprendre, communiquer et s’épanouir, peu importe son mode d’expression.

# 📚 MediaStock

**MediaStock** est un logiciel de bureau moderne conçu pour la gestion complète d'une médiathèque : bibliothèque,
ludothèque et vidéothèque.  
Développée en **Java 25** avec **JavaFX**, l'application propose une interface fluide, intuitive et pensée pour les
bibliothécaires.

---

## ✨ Fonctionnalités principales (v1)

### 📊 Tableau de bord

- Statistiques en temps réel sur les ressources enregistrées
- Nombre total de **Livres**, **DVD** et **Jeux de société**
- Suivi des exemplaires en retard

### 📦 Gestion de l'inventaire

- Ajout, modification et suppression de produits
- Gestion de plusieurs types de médias : **Livres**, **DVD**, **Jeux de société**
- Recherche dynamique et pagination fluide
- Génération automatique de **codes-barres EAN-13** pour chaque exemplaire physique

### 👥 Gestion des adhérents

- Inscription et modification des membres
- Consultation de l'historique complet des emprunts
- Fenêtre dédiée avec pagination

### 🔄 Emprunts & retours

- Système de scan par **code-barres** pour accélérer les opérations
- Vérification automatique des règles métiers :
    - quota maximum d'emprunts
    - disponibilité de l'exemplaire
    - état du produit
- Détection des retards depuis le tableau de bord

---

## 🏗️ Architecture du projet

Le projet repose sur une architecture en couches de type **MVC étendu** afin de garantir une base de code claire,
maintenable et évolutive.

```text
/model       → Entités métiers (Livre, DVD, JeuSociete, Adherent, Emprunt, Exemplaire)
/dao         → Accès aux données (requêtes SQL / MySQL)
/service     → Logique métier (règles d'emprunt, génération EAN13, orchestration)
/controller  → Interface JavaFX et gestion des événements utilisateur
```

### Objectifs de cette architecture

- Séparation claire des responsabilités
- Maintenance facilitée
- Évolutivité du projet
- Meilleure lisibilité du code

---

## 🚀 Installation & démarrage

### Prérequis

- **Java JDK 25+**
- **JavaFX** (si non inclus dans la distribution)
- **MySQL 8.0+**
- **Maven**
- Un IDE recommandé :
    - **IntelliJ IDEA**
    - **Eclipse**
    - **VS Code**

### 1. Cloner le dépôt

```bash
git clone https://github.com/toto49/mediastock.git
cd mediastock
```

### 2. Configurer la base de données

Lance ton serveur MySQL puis crée la base de données :

```sql
CREATE
DATABASE mediastock;
```

Ensuite, importe le script SQL de structure situé dans le dossier `/sql`.

### 3. Configurer les variables d'environnement

Crée un fichier `.env` à la racine du projet avec le contenu suivant :

```env
DB_URL=jdbc:mysql://ip:port/mediastock
DB_USER=root
DB_PASSWORD=VotreMotDePasse
```

### 4. Compiler et exécuter

#### Avec Maven

```bash
mvn clean install
mvn javafx:run
```

---

## 📸 Aperçu de l'interface

### Tableau de bord

![Tableau de bord](docs/images/dashboard.png)

### Gestion des retards

![Gestion des retards](docs/images/emprunts.png)

### Inventaire

![Inventaire](docs/images/inventaire.png)

---

## 📚 Documentation détaillée

- [Objectifs de cette architecture](docs/01-objectifs-architecture.md)
- [Prérequis](docs/02-prerequis.md)
- [1. Cloner le dépôt](docs/03-cloner-le-depot.md)
- [2. Configurer la base de données](docs/04-configurer-la-base-de-donnees.md)
- [3. Configurer les variables d'environnement](docs/05-configurer-le-fichier-env.md)
- [4. Compiler et exécuter](docs/06-compiler-et-executer.md)
- [5. Aperçu de l'interface](docs/07-apercu-interface.md)
- [6. Contribuer](docs/08-contribuer.md)
- [7. Licence](docs/09-licence.md)

---

## 🤝 Contribuer

Les contributions sont les bienvenues.

Merci de consulter les fichiers suivants avant toute contribution :

- `CONTRIBUTING.md`
- `CODE_OF_CONDUCT.md`
- `SECURITY.md`

Pour signaler une faille de sécurité, merci de suivre les instructions indiquées dans `SECURITY.md`.

---

## 📄 Licence

Ce projet est distribué sous licence **MIT**.  
Voir le fichier [`LICENSE`](LICENSE) pour plus de détails.

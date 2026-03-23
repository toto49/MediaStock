# 📚 MediaStock

[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](#)
[![JavaFX](https://img.shields.io/badge/JavaFX-GUI-4780bc?style=for-the-badge&logo=java&logoColor=white)](#)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](#)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](#)

**MediaStock** est un logiciel de bureau moderne conçu pour la gestion complète d’une médiathèque : bibliothèque,
ludothèque et vidéothèque.  
Développé en **Java 25** avec **JavaFX**, l’application propose une interface fluide, intuitive et pensée pour faciliter
le travail quotidien des bibliothécaires.

---

## ✨ Fonctionnalités principales

### 📊 Tableau de bord

- **Vue d’ensemble :** statistiques en temps réel sur les ressources enregistrées (livres, DVD, jeux de société).
- **Alertes :** suivi automatisé et affichage direct des exemplaires en retard.

### 📦 Gestion de l’inventaire

- **Multi-médias :** CRUD complet (ajout, modification, suppression) pour les différents types de médias.
- **Efficacité :** recherche dynamique, tri et pagination fluide des données.
- **Technologie :** génération automatique de **codes-barres EAN-13** pour faciliter le suivi physique des exemplaires.

### 👥 Gestion des adhérents & emprunts

- **Suivi complet :** inscription, modification des membres et consultation de l’historique de prêt.
- **Scan rapide :** système de scan par **webcam intégrée** (lecture de codes-barres) pour accélérer les retours et les
  emprunts.
- **Sécurité métier :** vérification automatique des règles (quota maximum d’emprunts, disponibilité de l’exemplaire).

---

## 🛠️ Stack technique & architecture

Le projet repose sur une architecture en couches de type **MVC (Modèle-Vue-Contrôleur)** afin de garantir une base de
code claire, sécurisée et maintenable.

- **Langage :** Java 25
- **Interface graphique :** JavaFX (avec Scene Builder & FXML)
- **Base de données :** MySQL (JDBC direct)
- **Outils externes :** ZXing (lecture/génération de codes-barres), JavaCV (webcam), JBCrypt (sécurité des mots de
  passe), Dotenv (variables d’environnement)

```text
/model       → Entités métiers (Livre, DVD, Adherent, Emprunt...)
/dao         → Accès aux données sécurisé (PreparedStatement)
/service     → Logique métier (règles d’emprunt, orchestration)
/controller  → Interface JavaFX et gestion des événements utilisateur
```

---

## 🚀 Installation & démarrage

### Prérequis

- **Java JDK 25+** installé
- **MySQL 8.0+** fonctionnel
- **Maven** (inclus via le wrapper `mvnw` du projet)

### 1. Préparation de la base de données

Connectez-vous à votre serveur MySQL et créez la base de données :

```sql
CREATE DATABASE mediastock;
```

Ensuite, importez la structure des tables en exécutant le script fourni : `sql/mediastock_Vierge.sql`.

### 2. Configuration sécurisée

Créez un fichier nommé exactement `.env` à la racine du projet (au même niveau que le fichier `pom.xml`) et ajoutez-y
vos identifiants MySQL :

```env
DB_URL=jdbc:mysql://localhost:3306/mediastock
DB_USER=root
DB_PASSWORD=votre_mot_de_passe_ici
```

### 3. Compilation et lancement

Ouvrez un terminal à la racine du projet et exécutez les commandes suivantes :

```bash
# Compiler le projet et télécharger les dépendances
mvn clean install

# Lancer l'application JavaFX
mvn javafx:run
```

---

## 📖 Documentation technique (Javadoc)

Conformément aux bonnes pratiques de versionnement avec Git, les fichiers HTML générés automatiquement (comme la
Javadoc) ne sont pas inclus dans ce dépôt. Ce choix permet de :

- garder un historique Git plus propre,
- éviter les conflits de fusion sur des fichiers générés,
- ne pas alourdir inutilement le dépôt.

### Méthode 1 — Génération via Maven en ligne de commande

Depuis la racine du projet, exécutez :

```bash
mvn javadoc:javadoc
```

Les fichiers générés seront disponibles dans :

```text
target/site/apidocs/
```

### Méthode 2 — Génération via IntelliJ IDEA avec Maven

Si vous utilisez **IntelliJ IDEA**, vous pouvez générer la Javadoc directement depuis l’interface :

1. Ouvrez l’onglet **Maven** situé dans la barre latérale droite d’IntelliJ IDEA.
2. Déroulez votre projet **MediaStock**.
3. Ouvrez la section **Plugins**.
4. Déroulez **javadoc**.
5. Double-cliquez sur **javadoc:javadoc**.

Vous pouvez également retrouver la documentation générée dans le même dossier :

```text
target/site/apidocs/
```

> 💡 **Astuce :** si l’onglet Maven n’apparaît pas dans IntelliJ IDEA, vérifiez que le projet a bien été importé comme
> projet **Maven** à partir du fichier `pom.xml`.

---

## 📸 Aperçu de l’interface

### Tableau de bord

![Tableau de bord](docs/images/dashboard.png)

### Gestion des retards

![Gestion des retards](docs/images/emprunts.png)

### Inventaire

![Inventaire](docs/images/inventaire.png)

---

## 🤝 Contribuer & documentation

Les contributions sont les bienvenues ! Merci de consulter les fichiers suivants avant toute Pull Request :

- [`CONTRIBUTING.md`](CONTRIBUTING.md) : règles de contribution
- [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md) : code de conduite
- [`SECURITY.md`](SECURITY.md) : signalement de failles

Retrouvez la documentation complète dans le dossier [`/docs`](docs/README-DOCS.md).

---

## 📄 Licence

Ce projet est distribué sous licence **MIT**. Voir le fichier [`LICENSE`](LICENSE) pour plus de détails.

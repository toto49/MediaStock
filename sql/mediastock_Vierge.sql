-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mer. 18 mars 2026 à 15:41
-- Version du serveur : 10.11.11-MariaDB
-- Version de PHP : 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `mediastock`
--

-- --------------------------------------------------------

--
-- Structure de la table `ADHERENT`
--

CREATE TABLE `ADHERENT` (
  `id` char(12) NOT NULL,
  `num_tel` varchar(20) DEFAULT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Structure de la table `ADMINISTRATEUR`
--

CREATE TABLE `ADMINISTRATEUR` (
  `id` int(11) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mdp` varchar(100) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `num_tel` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Structure de la table `EMPRUNT`
--

CREATE TABLE `EMPRUNT` (
  `id` int(11) NOT NULL,
  `date_debut` date DEFAULT NULL,
  `date_retour` date DEFAULT NULL,
  `id_adherent` char(12) NOT NULL,
  `id_exemplaire` int(11) DEFAULT NULL,
  `statut` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Structure de la table `EXEMPLAIRE`
--

CREATE TABLE `EXEMPLAIRE` (
  `id` int(11) NOT NULL,
  `code_barre` varchar(20) NOT NULL,
  `etat` varchar(50) DEFAULT NULL,
  `statut` varchar(50) DEFAULT NULL,
  `id_produit` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Structure de la table `PRODUIT`
--

CREATE TABLE `PRODUIT` (
  `id` int(11) NOT NULL,
  `type_produit` varchar(50) DEFAULT NULL,
  `titre` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `editeur` varchar(255) DEFAULT NULL,
  `annee_sortie` int(11) DEFAULT NULL,
  `isbn` int(11) DEFAULT NULL,
  `auteur` varchar(50) DEFAULT NULL,
  `nb_pages` int(11) DEFAULT NULL,
  `format` varchar(50) DEFAULT NULL,
  `realisateur` varchar(50) DEFAULT NULL,
  `duree_minutes` int(11) DEFAULT NULL,
  `audio_langues` varchar(100) DEFAULT NULL,
  `sous_titres` varchar(100) DEFAULT NULL,
  `nb_joueurs_min` int(11) DEFAULT NULL,
  `nb_joueurs_max` int(11) DEFAULT NULL,
  `age_min` int(11) DEFAULT NULL,
  `duree_partie` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `ADHERENT`
--
ALTER TABLE `ADHERENT`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_adh` (`id`,`nom`,`prenom`);

--
-- Index pour la table `ADMINISTRATEUR`
--
ALTER TABLE `ADMINISTRATEUR`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_email` (`email`),
  ADD KEY `idx_admin` (`id`,`email`,`nom`,`prenom`,`mdp`);

--
-- Index pour la table `EMPRUNT`
--
ALTER TABLE `EMPRUNT`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_exemplaire` (`id_exemplaire`),
  ADD KEY `EMPRUNT_ibfk_1` (`id_adherent`),
  ADD KEY `idx_emprunt` (`id`,`statut`,`id_adherent`,`id_exemplaire`);

--
-- Index pour la table `EXEMPLAIRE`
--
ALTER TABLE `EXEMPLAIRE`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_produit` (`id_produit`),
  ADD KEY `idx_exemplaire` (`id`,`id_produit`,`code_barre`);

--
-- Index pour la table `PRODUIT`
--
ALTER TABLE `PRODUIT`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_produit` (`id`,`type_produit`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `ADMINISTRATEUR`
--
ALTER TABLE `ADMINISTRATEUR`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=134;

--
-- AUTO_INCREMENT pour la table `EMPRUNT`
--
ALTER TABLE `EMPRUNT`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `EXEMPLAIRE`
--
ALTER TABLE `EXEMPLAIRE`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=336;

--
-- AUTO_INCREMENT pour la table `PRODUIT`
--
ALTER TABLE `PRODUIT`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `EMPRUNT`
--
ALTER TABLE `EMPRUNT`
  ADD CONSTRAINT `EMPRUNT_ibfk_1` FOREIGN KEY (`id_adherent`) REFERENCES `ADHERENT` (`id`),
  ADD CONSTRAINT `EMPRUNT_ibfk_2` FOREIGN KEY (`id_exemplaire`) REFERENCES `EXEMPLAIRE` (`id`);

--
-- Contraintes pour la table `EXEMPLAIRE`
--
ALTER TABLE `EXEMPLAIRE`
  ADD CONSTRAINT `EXEMPLAIRE_ibfk_1` FOREIGN KEY (`id_produit`) REFERENCES `PRODUIT` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

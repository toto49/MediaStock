-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mar. 24 mars 2026 à 15:36
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
-- Déchargement des données de la table `ADHERENT`
--

INSERT INTO `ADHERENT` (`id`, `num_tel`, `nom`, `prenom`, `email`) VALUES
('ADH-2026-001', '06 12 34 56 78', 'Dupont', 'Jean', 'jean.dupont@example.fr'),
('ADH-2026-002', '07 89 01 23 45', 'Martin', 'Sophie', 's.martin.pro@email.com'),
('ADH-2026-003', '06 54 32 10 97', 'Bernards', 'Lucass', 'lucas.bernard99@test.net'),
('ADH-2026-004', '07 11 22 33 44', 'Petit-Lefebvre', 'Emma', 'emma.petit-lefebvre@mail.fr'),
('ADH-2026-005', '06 99 88 77 66', 'Dubois', 'Thomas', 't.dubois@exemple.com'),
('ADH-2026-006', '07 55 44 33 22', 'Leroy', 'Chloé', 'chloe_leroy@courriel.fr');

-- --------------------------------------------------------

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
-- Déchargement des données de la table `ADMINISTRATEUR`
--

INSERT INTO `ADMINISTRATEUR` (`id`, `email`, `mdp`, `prenom`, `nom`, `num_tel`) VALUES
(4, 'autre@test.com', '$2a$12$5/wBZCgkSMtT/mRdEZrhNuJ42SuPT3nyVRu.7m8WDnddguvTrCQza', 'Jean', 'Dupont', 'abcdefghij'),
(8, 'lj@jtr.fr', '$2a$12$gySDlfly3ydvBvVP3OQ2hOJz4cZD2zoK3uiet5CzQk9ZlsKTcswAS', 'Lilian', 'Jautrou', '0636402771'),
(9, 'tom@tom.fr', '$2a$12$djemb.RbpGRZSqdg/l0GH.hVz5BZTvm4mG69VYFH4BCiiTd9VhXHO', 'tom', 'boudaud', '0123456789'),
(11, 'test@admin.com', '$2a$12$bI60EVE2wdI/UOIWr/wD.eepsnpFfy8KY0acirdPo7u/oY11KmoFW', 'Nom', 'Autre', '0623456789'),
(14, 'unique_1774272411995@test.com1', '$2a$12$zn51PMEMTlSChzNPKQx0yu34tgKHup3zGJCB/clEt4vSiEM0Un2IG', 'Jean', 'Dupont', 'abcdefghij'),
(18, 'remi.goubin@test.com', '$2a$12$R.AtFouOvAdkdYSQ0p09R.wzF4BiWZB/UcZn5V2oyaNm/8hvpmRge', '  ', '  ', '0601010101');

-- --------------------------------------------------------

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
-- Déchargement des données de la table `EMPRUNT`
--

INSERT INTO `EMPRUNT` (`id`, `date_debut`, `date_retour`, `id_adherent`, `id_exemplaire`, `statut`) VALUES
(1, '2026-03-24', '2026-03-23', 'ADH-2026-006', 92, 'EMPRUNTE'),
(2, '2026-03-24', '2026-05-24', 'ADH-2026-004', 79, 'EMPRUNTE'),
(3, '2026-03-24', '2026-05-24', 'ADH-2026-004', 65, 'RENDU'),
(4, '2026-03-24', '2026-05-24', 'ADH-2026-006', 72, 'EMPRUNTE');

-- --------------------------------------------------------

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
-- Déchargement des données de la table `EXEMPLAIRE`
--

INSERT INTO `EXEMPLAIRE` (`id`, `code_barre`, `etat`, `statut`, `id_produit`) VALUES
(35, '2151642001332', 'NEUF', 'DISPONIBLE', 26),
(36, '2125230563069', 'NEUF', 'DISPONIBLE', 26),
(37, '2196021353251', 'NEUF', 'DISPONIBLE', 26),
(38, '2191789507793', 'NEUF', 'DISPONIBLE', 26),
(39, '2122956884003', 'NEUF', 'DISPONIBLE', 26),
(40, '2143234416457', 'NEUF', 'DISPONIBLE', 27),
(41, '2140500181644', 'NEUF', 'DISPONIBLE', 27),
(42, '2133819727856', 'NEUF', 'DISPONIBLE', 27),
(43, '2184822812459', 'NEUF', 'DISPONIBLE', 27),
(44, '2129323558673', 'NEUF', 'DISPONIBLE', 27),
(45, '2142039057667', 'NEUF', 'DISPONIBLE', 27),
(46, '2184067637541', 'NEUF', 'DISPONIBLE', 27),
(47, '2186400308286', 'NEUF', 'DISPONIBLE', 27),
(48, '2186724368645', 'NEUF', 'DISPONIBLE', 28),
(49, '2194530163439', 'NEUF', 'DISPONIBLE', 28),
(50, '2119196360756', 'NEUF', 'DISPONIBLE', 28),
(51, '2130204944126', 'NEUF', 'DISPONIBLE', 29),
(52, '2124841860840', 'NEUF', 'DISPONIBLE', 29),
(53, '2182904487489', 'NEUF', 'DISPONIBLE', 30),
(54, '2135157856434', 'NEUF', 'DISPONIBLE', 30),
(55, '2134374493415', 'NEUF', 'DISPONIBLE', 30),
(56, '2119370013997', 'NEUF', 'DISPONIBLE', 30),
(57, '2154854046047', 'NEUF', 'DISPONIBLE', 31),
(58, '2166169988547', 'NEUF', 'DISPONIBLE', 31),
(59, '2155696900115', 'NEUF', 'DISPONIBLE', 31),
(60, '2113201023780', 'NEUF', 'DISPONIBLE', 31),
(61, '2148481870441', 'NEUF', 'DISPONIBLE', 31),
(62, '2147020917074', 'NEUF', 'DISPONIBLE', 31),
(63, '2354212995301', 'NEUF', 'DISPONIBLE', 32),
(64, '2321170774378', 'NEUF', 'DISPONIBLE', 32),
(65, '2356936031228', 'NEUF', 'DISPONIBLE', 32),
(66, '2386854781301', 'NEUF', 'DISPONIBLE', 33),
(67, '2385158674180', 'NEUF', 'DISPONIBLE', 34),
(68, '2348757349804', 'NEUF', 'DISPONIBLE', 34),
(69, '2339601761811', 'NEUF', 'DISPONIBLE', 35),
(70, '2321014282861', 'NEUF', 'DISPONIBLE', 35),
(71, '2338093392169', 'NEUF', 'DISPONIBLE', 35),
(72, '2394497508087', 'NEUF', 'EMPRUNTE', 36),
(73, '2381822980359', 'NEUF', 'DISPONIBLE', 37),
(74, '2313250063489', 'NEUF', 'DISPONIBLE', 37),
(75, '2327621041723', 'NEUF', 'DISPONIBLE', 37),
(76, '2399498639808', 'NEUF', 'DISPONIBLE', 37),
(77, '2280865435508', 'NEUF', 'DISPONIBLE', 38),
(78, '2228276123577', 'NEUF', 'DISPONIBLE', 38),
(79, '2267901278329', 'NEUF', 'EMPRUNTE', 38),
(80, '2221802775684', 'NEUF', 'DISPONIBLE', 38),
(81, '2217687152429', 'NEUF', 'DISPONIBLE', 39),
(82, '2272062093698', 'NEUF', 'DISPONIBLE', 39),
(83, '2274184011758', 'NEUF', 'DISPONIBLE', 40),
(84, '2291074641796', 'NEUF', 'DISPONIBLE', 40),
(85, '2240691579708', 'NEUF', 'DISPONIBLE', 40),
(86, '2249867353388', 'NEUF', 'DISPONIBLE', 40),
(87, '2290238186630', 'NEUF', 'DISPONIBLE', 40),
(88, '2275895102841', 'NEUF', 'DISPONIBLE', 41),
(89, '2222994621810', 'NEUF', 'DISPONIBLE', 41),
(90, '2288972796291', 'NEUF', 'DISPONIBLE', 41),
(91, '2269413272239', 'NEUF', 'DISPONIBLE', 42),
(92, '2264413103477', 'NEUF', 'EMPRUNTE', 42),
(93, '2259387509530', 'NEUF', 'DISPONIBLE', 43),
(94, '2287250663676', 'NEUF', 'DISPONIBLE', 43),
(95, '2229070309884', 'NEUF', 'DISPONIBLE', 43);

-- --------------------------------------------------------

--
-- Structure de la table `PRODUIT`
--

CREATE TABLE `PRODUIT` (
  `id` int(11) NOT NULL,
  `type_produit` varchar(50) DEFAULT NULL,
  `titre` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `editeur` varchar(255) DEFAULT NULL,
  `annee_sortie` int(11) DEFAULT NULL,
  `isbn` varchar(255) DEFAULT NULL,
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
-- Déchargement des données de la table `PRODUIT`
--

INSERT INTO `PRODUIT` (`id`, `type_produit`, `titre`, `description`, `editeur`, `annee_sortie`, `isbn`, `auteur`, `nb_pages`, `format`, `realisateur`, `duree_minutes`, `audio_langues`, `sous_titres`, `nb_joueurs_min`, `nb_joueurs_max`, `age_min`, `duree_partie`) VALUES
(26, 'Livre', 'Dune', 'Sur la planète désertique Arrakis, la famille Atréides se bat pour le contrôle de l\'Épice, la ressource la plus précieuse de l\'univers. Le jeune Paul devra affronter son destin.', 'Pocket', 2020, '978-2266320481', 'Frank Herbert', 832, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(27, 'Livre', 'Le Petit Prince', 'Un aviateur perdu dans le désert du Sahara rencontre un jeune garçon venu d\'une autre planète, qui lui raconte ses voyages et ses rencontres absurdes ou poétiques.', 'Gallimard', 1946, '978-2070612758', 'Antoine de Saint-Exupéry', 120, 'Broché', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(28, 'Livre', 'Shutter Island', 'En 1954, le marshal Teddy Daniels enquête sur la disparition mystérieuse d\'une patiente dans un hôpital psychiatrique de haute sécurité isolé sur une île.', 'Payot & Rivages', 2003, '978-2743620063', 'Dennis Lehane', 416, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(29, 'Livre', 'Le Seigneur des Anneaux : La Communauté de l\'Anneau', 'Frodon Sacquet hérite d\'un anneau magique doté d\'un pouvoir maléfique. Accompagné de ses amis, il doit entreprendre un périlleux voyage pour le détruire.', 'Christian Bourgois', 2014, '978-2267027006', 'J.R.R. Tolkien', 528, 'Broché', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(30, 'Livre', 'Orgueil et Préjugés', 'Dans l\'Angleterre rurale du XIXe siècle, Elizabeth Bennet, jeune femme intelligente et spirituelle, fait la rencontre du riche mais hautain Mr Darcy.', 'Folio', 2007, '978-2070338666', 'Jane Austen', 448, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(31, 'Livre', 'Sapiens : Une brève histoire de l\'humanité', 'Comment l\'Homo sapiens a-t-il réussi à dominer la Terre ? Une exploration fascinante de l\'histoire de notre espèce, de l\'âge de pierre à nos jours.', 'Albin Michel', 2015, '978-2226257017', 'Yuval Noah Harari', 512, 'Broché', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(32, 'Jeu', 'Catan', 'Construisez des colonies, des villes et des routes sur l\'île vierge de Catane en récoltant et en échangeant astucieusement des ressources avec les autres joueurs.', 'Asmodee', 1995, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 4, 10, 75),
(33, 'Jeu', 'Les Aventuriers du Rail', 'Collectez des cartes wagons pour prendre le contrôle de chemins de fer reliant les différentes villes d\'Amérique du Nord. Un jeu de plateau stratégique et accessible.', 'Days of Wonder', 2004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 5, 8, 45),
(34, 'Jeu', '7 Wonders', 'Prenez la tête de l\'une des sept grandes cités du monde antique et faites-la prospérer par le commerce, les découvertes scientifiques et la force militaire.', 'Repos Production', 2010, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 7, 10, 30),
(35, 'Jeu', 'Dixit', 'Un jeu poétique où les joueurs doivent deviner et faire deviner de magnifiques cartes aux illustrations oniriques à partir d\'indices subtils.', 'Libellud', 2008, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 6, 8, 30),
(36, 'Jeu', 'Gloomhaven', 'Un jeu de plateau coopératif et tactique de dark fantasy où les joueurs incarnent des mercenaires explorant des donjons et faisant évoluer leurs personnages.', 'Cephalofair Games', 2017, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 4, 14, 120),
(37, 'Jeu', 'Azul', 'Incarnez un artisan chargé de décorer les murs du palais royal d\'Evora avec de magnifiques azulejos, des carreaux de céramique colorés.', 'Plan B Games', 2017, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 4, 8, 45),
(38, 'DVD', 'Matrix', 'Un pirate informatique découvre que la réalité n\'est qu\'une simulation virtuelle créée par des machines et rejoint une rébellion pour libérer l\'humanité.', 'Warner Bros.', 1999, NULL, NULL, NULL, NULL, 'Lana et Lilly Wachowski', 136, 'Français,Anglais', 'Anglais,Français,Espagnol', NULL, NULL, NULL, NULL),
(39, 'DVD', 'Le Dîner de Cons', 'Un groupe d\'amis organise chaque semaine un dîner où chacun doit amener un \"con\". Celui qui a déniché le plus spectaculaire est déclaré vainqueur.', 'Gaumont', 1998, NULL, NULL, NULL, NULL, 'Francis Veber', 80, 'Français', 'Français, Chinois', NULL, NULL, NULL, NULL),
(40, 'DVD', 'Le Seigneur des Anneaux : Le Retour du Roi', 'L\'ultime bataille pour la Terre du Milieu commence. Frodon et Sam s\'approchent de la Montagne du Destin pour détruire l\'Anneau Unique.', 'Metropolitan FilmExport', 2003, NULL, NULL, NULL, NULL, 'Peter Jackson', 201, 'Français,Anglais', 'Français audiodescription', NULL, NULL, NULL, NULL),
(41, 'DVD', 'Le Voyage de Chihiro', 'Une fillette de dix ans s\'égare avec ses parents dans un monde peuplé d\'esprits, de dieux et de créatures magiques, et doit trouver un moyen de les sauver.', 'Studio Ghibli / Buena Vista', 2001, NULL, NULL, NULL, NULL, 'Hayao Miyazaki', 125, 'Français,Japonais', 'Anglais,Français', NULL, NULL, NULL, NULL),
(42, 'DVD', 'Parasite', 'La famille Ki-taek, au chômage, s\'immisce peu à peu dans la vie de la riche famille Park, déclenchant une série d\'événements incontrôlables.', 'The Jokers', 2019, NULL, NULL, NULL, NULL, 'Bong Joon Ho', 132, 'Français,Coréen', 'Anglais', NULL, NULL, NULL, NULL),
(43, 'DVD', 'Titanic', 'L\'histoire d\'amour tragique entre Jack, un artiste pauvre, et Rose, une jeune femme de la haute société, à bord du célèbre paquebot insubmersible.', '20th Century Fox', 1997, NULL, NULL, NULL, NULL, 'James Cameron', 194, 'Français,Anglais', 'Français', NULL, NULL, NULL, NULL);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT pour la table `EMPRUNT`
--
ALTER TABLE `EMPRUNT`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `EXEMPLAIRE`
--
ALTER TABLE `EXEMPLAIRE`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=96;

--
-- AUTO_INCREMENT pour la table `PRODUIT`
--
ALTER TABLE `PRODUIT`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

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

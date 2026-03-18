-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mer. 18 mars 2026 à 08:27
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
('ADH-', '+33 6 12 34 56 78', 'Étienne', 'François', 'etienne@email.com'),
('ADH-2026-001', '0101010101', 'remi', 'remi', 'test@test.com'),
('ADH-2026-002', '1496151', 'edzfdf', 'ercferf', 'testtttt@nnn.com'),
('ADH-2026-003', 'sqfvfdvdqf', 'fdqfg', 'qsfvgqfg', 'sfqvfdgf@sdfergfq.fr'),
('ADH-2754654', '0123456789', 'LicornTestmodif', 'MorgianeTest', 'email@test.com'),
('ADH-9849849', '0612345678', 'Long', 'Email', 'email.tres.tres.long@test.domain.com');

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
(116, 'test@test.com', '$2a$12$400HI7W2S8fiZoWPvovTkOQ3dyen5E8MwB2jFBZLmvLFfbs77QWHm', 'tome', 'boudaud', '59481817'),
(117, 'remi@remi.com', '$2a$12$O/m5WmbSaXxNmh/y8NllwerKs8fQR3R7.5nyQa4AKbEpF2rEHGDua', 'Rémi', 'Rémi', '101010101'),
(120, 'remi2@remi.com', '$2a$12$xtbpc1Bz/gdRArJngIIoG.B7zm7qdclcP2RO1SPq39HOdsiWnZbTW', 'remi2', 'remi2', '202020202'),
(121, 'test@test.fr', '$2a$12$zOWEK3Y3.7dvekBr4lciH.QDLELJasIzcYPNT5GGoe.gquWsaKon6', 'morgiane', 'morgiane', '652369874'),
(122, 'lj@jtr.fr', '$2a$12$SCoo/IeEu5qY6YQOKYR1U.KDRENyFtSbDV2y8XPMQjuopPblOjpra', 'Lilian', 'Jautrou', '636402770');

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
(1, '2026-03-08', '2026-03-10', 'ADH-2026-001', 2, 'EMPRUNTE'),
(2, '2026-03-16', '2026-05-16', 'ADH-2026-002', 3, 'RENDU');

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
(2, '2149238081165', 'NEUF', 'EMPRUNTE', 22),
(3, '2101707002545', 'NEUF', 'DISPONIBLE', 22),
(4, '2133234552095', 'NEUF', 'DISPONIBLE', 22),
(5, '2231964908400', 'NEUF', 'DISPONIBLE', 23),
(6, '2200682328319', 'NEUF', 'DISPONIBLE', 23),
(7, '2225742069551', 'NEUF', 'DISPONIBLE', 23),
(8, '2239280298311', 'NEUF', 'DISPONIBLE', 23),
(9, '2231086220381', 'NEUF', 'DISPONIBLE', 23),
(10, '2265140847344', 'NEUF', 'DISPONIBLE', 23),
(11, '2168366701998', 'NEUF', 'DISPONIBLE', 24),
(12, '2144830472151', 'NEUF', 'DISPONIBLE', 24),
(13, '2154417983581', 'NEUF', 'DISPONIBLE', 24),
(14, '2102013296352', 'NEUF', 'DISPONIBLE', 24),
(15, '2119397835336', 'NEUF', 'DISPONIBLE', 24),
(16, '2186949446197', 'NEUF', 'DISPONIBLE', 24),
(17, '2246612210125', 'NEUF', 'DISPONIBLE', 25),
(18, '2213643978515', 'NEUF', 'DISPONIBLE', 25),
(19, '2115110395224', 'NEUF', 'DISPONIBLE', 26),
(20, '2177479652387', 'NEUF', 'DISPONIBLE', 26),
(21, '2253057047216', 'NEUF', 'DISPONIBLE', 27),
(22, '2304167179882', 'NEUF', 'DISPONIBLE', 28),
(23, '2155356193963', 'NEUF', 'DISPONIBLE', 29),
(24, '2165430190559', 'NEUF', 'DISPONIBLE', 29),
(25, '2222063624773', 'NEUF', 'DISPONIBLE', 30),
(26, '2312626237530', 'NEUF', 'DISPONIBLE', 31),
(27, '2122165339035', 'NEUF', 'DISPONIBLE', 32),
(28, '2187539494833', 'NEUF', 'DISPONIBLE', 32),
(29, '2149900036608', 'NEUF', 'DISPONIBLE', 32),
(30, '2142157816092', 'NEUF', 'DISPONIBLE', 32),
(31, '2129612274512', 'NEUF', 'DISPONIBLE', 32),
(32, '2113569396489', 'NEUF', 'DISPONIBLE', 32),
(33, '2139027974440', 'NEUF', 'DISPONIBLE', 32),
(34, '2118214101371', 'NEUF', 'DISPONIBLE', 32),
(35, '2122444250402', 'NEUF', 'DISPONIBLE', 32),
(36, '2169873879514', 'NEUF', 'DISPONIBLE', 32),
(37, '2161858406906', 'NEUF', 'DISPONIBLE', 32),
(38, '2141774901723', 'NEUF', 'DISPONIBLE', 32),
(39, '2169842440356', 'NEUF', 'DISPONIBLE', 32),
(40, '2108940610827', 'NEUF', 'DISPONIBLE', 32),
(41, '2177028818950', 'NEUF', 'DISPONIBLE', 32),
(42, '2122983397064', 'NEUF', 'DISPONIBLE', 32),
(43, '2189511327122', 'NEUF', 'DISPONIBLE', 32),
(44, '2149025754579', 'NEUF', 'DISPONIBLE', 32),
(45, '2107199420799', 'NEUF', 'DISPONIBLE', 32),
(46, '2137140720296', 'NEUF', 'DISPONIBLE', 32),
(47, '2147033499529', 'NEUF', 'DISPONIBLE', 32),
(48, '2146574898099', 'NEUF', 'DISPONIBLE', 32),
(49, '2167716045287', 'NEUF', 'DISPONIBLE', 32),
(50, '2133200007918', 'NEUF', 'DISPONIBLE', 32),
(51, '2179390047068', 'NEUF', 'DISPONIBLE', 32),
(52, '2159921765838', 'NEUF', 'DISPONIBLE', 32),
(53, '2140558167447', 'NEUF', 'DISPONIBLE', 32),
(54, '2157105281280', 'NEUF', 'DISPONIBLE', 32),
(55, '2189946618758', 'NEUF', 'DISPONIBLE', 32),
(56, '2159057499393', 'NEUF', 'DISPONIBLE', 32),
(57, '2106345778678', 'NEUF', 'DISPONIBLE', 32),
(58, '2101999374351', 'NEUF', 'DISPONIBLE', 32),
(59, '2169480715533', 'NEUF', 'DISPONIBLE', 32),
(60, '2175179307354', 'NEUF', 'DISPONIBLE', 32),
(61, '2128360517728', 'NEUF', 'DISPONIBLE', 32),
(62, '2176397438448', 'NEUF', 'DISPONIBLE', 32),
(63, '2135413525715', 'NEUF', 'DISPONIBLE', 32),
(64, '2145455795007', 'NEUF', 'DISPONIBLE', 32),
(65, '2119030560199', 'NEUF', 'DISPONIBLE', 32),
(66, '2108307244528', 'NEUF', 'DISPONIBLE', 32),
(67, '2159310148709', 'NEUF', 'DISPONIBLE', 32),
(68, '2182987317222', 'NEUF', 'DISPONIBLE', 32),
(69, '2111135454236', 'NEUF', 'DISPONIBLE', 32),
(70, '2167974839369', 'NEUF', 'DISPONIBLE', 32),
(71, '2170924862331', 'NEUF', 'DISPONIBLE', 32),
(72, '2149293186430', 'NEUF', 'DISPONIBLE', 32),
(73, '2173106182289', 'NEUF', 'DISPONIBLE', 32),
(74, '2144443837644', 'NEUF', 'DISPONIBLE', 32),
(75, '2135593471024', 'NEUF', 'DISPONIBLE', 32),
(76, '2146823618294', 'NEUF', 'DISPONIBLE', 32),
(77, '2140227422914', 'NEUF', 'DISPONIBLE', 32),
(78, '2181438919671', 'NEUF', 'DISPONIBLE', 32),
(79, '2110612720642', 'NEUF', 'DISPONIBLE', 32),
(80, '2171972788376', 'NEUF', 'DISPONIBLE', 32),
(81, '2111293070217', 'NEUF', 'DISPONIBLE', 32),
(82, '2166218832425', 'NEUF', 'DISPONIBLE', 32),
(83, '2105397120381', 'NEUF', 'DISPONIBLE', 32),
(84, '2184860371710', 'NEUF', 'DISPONIBLE', 32),
(85, '2133322768056', 'NEUF', 'DISPONIBLE', 32),
(86, '2160991535153', 'NEUF', 'DISPONIBLE', 32),
(87, '2159666378850', 'NEUF', 'DISPONIBLE', 32),
(88, '2100752527652', 'NEUF', 'DISPONIBLE', 32),
(89, '2125316290926', 'NEUF', 'DISPONIBLE', 32),
(90, '2130243774494', 'NEUF', 'DISPONIBLE', 32),
(91, '2173692117146', 'NEUF', 'DISPONIBLE', 32),
(92, '2159409195812', 'NEUF', 'DISPONIBLE', 32),
(93, '2145704322268', 'NEUF', 'DISPONIBLE', 32),
(94, '2138909781114', 'NEUF', 'DISPONIBLE', 32),
(95, '2104720348805', 'NEUF', 'DISPONIBLE', 32),
(96, '2112873188902', 'NEUF', 'DISPONIBLE', 32),
(97, '2187148234578', 'NEUF', 'DISPONIBLE', 32),
(98, '2125688435857', 'NEUF', 'DISPONIBLE', 32),
(99, '2134086643139', 'NEUF', 'DISPONIBLE', 32),
(100, '2139257527027', 'NEUF', 'DISPONIBLE', 32),
(101, '2126648201598', 'NEUF', 'DISPONIBLE', 32),
(102, '2113015558270', 'NEUF', 'DISPONIBLE', 32),
(103, '2100253769186', 'NEUF', 'DISPONIBLE', 32),
(104, '2149940969935', 'NEUF', 'DISPONIBLE', 32),
(105, '2128538043080', 'NEUF', 'DISPONIBLE', 32),
(106, '2100207743798', 'NEUF', 'DISPONIBLE', 32),
(107, '2134027010273', 'NEUF', 'DISPONIBLE', 32),
(108, '2160247775814', 'NEUF', 'DISPONIBLE', 32),
(109, '2142876886246', 'NEUF', 'DISPONIBLE', 32),
(110, '2109116452616', 'NEUF', 'DISPONIBLE', 32),
(111, '2123540214558', 'NEUF', 'DISPONIBLE', 32),
(112, '2139166132763', 'NEUF', 'DISPONIBLE', 32),
(113, '2101991851706', 'NEUF', 'DISPONIBLE', 32),
(114, '2142265753920', 'NEUF', 'DISPONIBLE', 32),
(115, '2128256074908', 'NEUF', 'DISPONIBLE', 32),
(116, '2161950849984', 'NEUF', 'DISPONIBLE', 32),
(117, '2183482621654', 'NEUF', 'DISPONIBLE', 32),
(118, '2141894494303', 'NEUF', 'DISPONIBLE', 32),
(119, '2143186974333', 'NEUF', 'DISPONIBLE', 32),
(120, '2140559279019', 'NEUF', 'DISPONIBLE', 32),
(121, '2188867878210', 'NEUF', 'DISPONIBLE', 32),
(122, '2144583324370', 'NEUF', 'DISPONIBLE', 32),
(123, '2176818160781', 'NEUF', 'DISPONIBLE', 32),
(124, '2100785582642', 'NEUF', 'DISPONIBLE', 32),
(125, '2123235235660', 'NEUF', 'DISPONIBLE', 32),
(126, '2170219361341', 'NEUF', 'DISPONIBLE', 32),
(127, '2140432112594', 'NEUF', 'DISPONIBLE', 33),
(128, '2112876946394', 'NEUF', 'DISPONIBLE', 33),
(129, '2222028794213', 'NEUF', 'DISPONIBLE', 34),
(130, '2300653061396', 'NEUF', 'DISPONIBLE', 35);

-- --------------------------------------------------------

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
-- Déchargement des données de la table `PRODUIT`
--

INSERT INTO `PRODUIT` (`id`, `type_produit`, `titre`, `description`, `editeur`, `annee_sortie`, `isbn`, `auteur`, `nb_pages`, `format`, `realisateur`, `duree_minutes`, `audio_langues`, `sous_titres`, `nb_joueurs_min`, `nb_joueurs_max`, `age_min`, `duree_partie`) VALUES
(1, 'Livre', 'Tom et le vilain Lilian', 'Il font des bébé dans les champs a l\'abris des regards', 'Lilian & Co', 2026, 1, 'Lilian Jautrou', 69, 'Poche', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL),
(3, 'Jeu', 'TomTom', 'Ba faut taper sur la tête de Tom', 'Boudaud & Co', 2020, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, 2, 8, 2, 20),
(6, 'DVD', 'Tom et Rémi dans le future ', 'Bah il vont dans le futur ', 'MorgianeEditions', 1990, NULL, NULL, NULL, NULL, 'Lilian Jautrou', 90, 'Français,Espagnol\r\n', 'Anglais,Indiens', NULL, NULL, NULL, NULL),
(7, 'DVD', 'EZF', 'ERFREF', 'RDFEFG', 2026, NULL, NULL, NULL, NULL, 'EFRR', 120, 'EG', 'ER', NULL, NULL, NULL, NULL),
(8, 'DVD', 'poney', 'pinguoin', 'girafe', 2026, NULL, NULL, NULL, NULL, 'surémis', 120, 'bloublou', 'hacker', NULL, NULL, NULL, NULL),
(9, 'JeuSociete', 'ezfre', 'frdef', 'rzfger', 2026, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 4, 12, 30),
(10, 'Livre', 'er', 'egerg', 'vrgg', 2026, 512961, 'rgr', 200, 'vrdgvzb', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(11, 'DVD', 'moi', 'moi', 'moi', 2020, NULL, NULL, NULL, NULL, 'moi', 120, 'moi', 'moi', NULL, NULL, NULL, NULL),
(12, 'DVD', 'Un titre de film', 'Une description de film', 'Un editeur de film', 2022, NULL, NULL, NULL, NULL, 'Le réalisateur de film', 108, 'FR', '', NULL, NULL, NULL, NULL),
(13, 'Livre', 'eef', 'efefef', 'efefef', 2022, 65464168, 'eezf', 203, 'efegrg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(14, 'DVD', 'fzrf', 'er\'fgv', 'eggreg', 2025, NULL, NULL, NULL, NULL, 'regre', 88, 'g', 'rgqreg', NULL, NULL, NULL, NULL),
(15, 'JeuSociete', 'ege', 'rsqgveg', 'eresgregrgr', 2021, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 6, 7, 33),
(16, 'Livre', 'aza', 'zaaz', 'azaz', 2020, 0, 'azaz', 200, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(17, 'Livre', 'dezfdxswcrv ', 'qgfvdserqgv', 'sedrfgve', 2022, 0, 'rgsedvgfrg', 195, 'ezfezsfzef', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(18, 'Livre', 'iojoij', 'oijijioj', 'iojjio', 2015, 0, 'ihjpjijpo', 183, 'poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(19, 'Jeu', 'azea', 'azeaze', 'azeaze', 2024, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 4, 12, 30),
(20, 'Jeu', 'effs', 'sdfsfd', 'sdfsfd', 2026, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 4, 12, 30),
(21, 'Jeu', 'qsdqsd', 'qsdqsd', 'qsdqsd', 2022, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 4, 12, 30),
(22, 'Livre', 'zesdd', 'sdsdf', 'sdfsfd', 2020, 0, 'dsfsd', 190, 'sddsf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(23, 'DVD', 'Metalorgie', 'Bah il font une orgie dans une usine', 'Michoucroute & Midas', 2020, NULL, NULL, NULL, NULL, 'Willam Alcolo', 69, 'Français,Japonais', NULL, NULL, NULL, NULL, NULL),
(24, 'Livre', 'd,;dsvfbn', 'dxc dcv', 'cd cvdcwdcv', 2026, 0, 'dv dxv ', 200, 'swqcdsd', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(25, 'DVD', 'fvr', 'ggrtegqg', 'zrgtr', 2026, NULL, NULL, NULL, NULL, 'gtrgrgr', 120, 'grg', NULL, NULL, NULL, NULL, NULL),
(26, 'Livre', 'Le Petit Prince', 'Conte philosophique', 'Gallimard', 1943, 0, 'Saint-Exupéry', 96, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(27, 'DVD', 'Inception', 'Film de science-fiction', 'Warner Bros', 2010, NULL, NULL, NULL, NULL, 'Nolan', 148, 'Français,Anglais', NULL, NULL, NULL, NULL, NULL),
(28, 'Jeu', 'Monopoly', 'Jeu de société classique', 'Hasbro', 1935, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 6, 8, 120),
(29, 'Livre', 'Le Petit Prince', 'Conte philosophique', 'Gallimard', 1943, 0, 'Saint-Exupéry', 96, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(30, 'DVD', 'Inception', 'Film', 'Warner Bros', 2010, NULL, NULL, NULL, NULL, 'Nolan', 148, 'Français,Anglais', NULL, NULL, NULL, NULL, NULL),
(31, 'Jeu', 'Monopoly', 'Jeu classique', 'Hasbro', 1935, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 6, 8, 120),
(32, 'Livre', 'azza', 'zaza', 'zazaza', 2022, 0, 'azeaz', 197, 'Pa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(33, 'Livre', 'Le Petit Prince', 'Conte philosophique', 'Gallimard', 1943, 0, 'Saint-Exupéry', 96, 'Poche', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(34, 'DVD', 'Inception', 'Film', 'Warner Bros', 2010, NULL, NULL, NULL, NULL, 'Nolan', 148, 'Français,Anglais', NULL, NULL, NULL, NULL, NULL),
(35, 'Jeu', 'Monopoly', 'Jeu classique', 'Hasbro', 1935, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 6, 8, 120);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `ADHERENT`
--
ALTER TABLE `ADHERENT`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `ADMINISTRATEUR`
--
ALTER TABLE `ADMINISTRATEUR`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_email` (`email`);

--
-- Index pour la table `EMPRUNT`
--
ALTER TABLE `EMPRUNT`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_exemplaire` (`id_exemplaire`),
  ADD KEY `EMPRUNT_ibfk_1` (`id_adherent`);

--
-- Index pour la table `EXEMPLAIRE`
--
ALTER TABLE `EXEMPLAIRE`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_produit` (`id_produit`);

--
-- Index pour la table `PRODUIT`
--
ALTER TABLE `PRODUIT`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `ADMINISTRATEUR`
--
ALTER TABLE `ADMINISTRATEUR`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=129;

--
-- AUTO_INCREMENT pour la table `EMPRUNT`
--
ALTER TABLE `EMPRUNT`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `EXEMPLAIRE`
--
ALTER TABLE `EXEMPLAIRE`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=131;

--
-- AUTO_INCREMENT pour la table `PRODUIT`
--
ALTER TABLE `PRODUIT`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

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

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 27 avr. 2025 à 01:16
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `eduskool`
--

-- --------------------------------------------------------

--
-- Structure de la table `activity`
--

CREATE TABLE `activity` (
  `id` int(11) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `description` longtext NOT NULL,
  `date` datetime NOT NULL,
  `image_file_name` varchar(255) DEFAULT NULL,
  `is_approved` tinyint(1) NOT NULL DEFAULT 0,
  `types_activity` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `activity`
--

INSERT INTO `activity` (`id`, `titre`, `description`, `date`, `image_file_name`, `is_approved`, `types_activity`, `created_at`) VALUES
(86, 'hobo', 'hobo', '2589-01-25 00:00:00', '', 1, 'aaaa', '2025-04-09 13:50:03'),
(87, 'qqqqqqqqqqqq', 'qqqqqqqqqqqqqqqqq', '2000-04-02 00:00:00', '', 1, 'ssssssssssss', '2025-04-11 17:16:52'),
(88, 'gadourheee', 'heeeeeeeeeeeeeee', '2025-04-25 00:00:00', 'soutenance.jpg', 1, 'aaaaaaaa', '2025-04-11 17:22:00'),
(101, 'exemple', 'description exemple .........................', '2025-04-12 20:49:00', '[\"67fac3eedd611.jpg\"]', 0, 'sport', '2025-04-12 19:50:06'),
(102, 'abdelkader', 'bonjour', '2025-04-12 00:00:00', 'img_1744804291360.png', 0, 'aaaaaaaa', '2025-04-16 12:51:38'),
(103, '', 'bonjour', '2025-04-12 00:00:00', 'img_1744804291360.png', 0, 'aaaaaaaa', '2025-04-16 12:52:02'),
(104, 'a', 's', '2025-04-18 00:00:00', '', 1, 's', '2025-04-16 13:08:45'),
(105, 'ssq', 'sqsq', '2025-04-25 00:00:00', '', 0, '', '2025-04-16 13:11:22'),
(106, 'sqs', 'sqs', '2025-04-11 00:00:00', '', 0, '', '2025-04-16 13:12:10'),
(107, 'aaaa', 'aaaaaaaaaaaaaaaaaa', '2027-04-10 00:00:00', 'img_1744805879632.jpg', 0, 'aaaaa', '2025-04-16 13:18:14'),
(108, 'aaaa', 'aaaaaaaaaaaaaaaaa', '2026-04-10 00:00:00', 'img_1744883580583.jpg', 0, 'sport', '2025-04-17 10:53:07'),
(109, 'exempleaaaaaaaaaaaa', 'description exemple .........................', '2028-04-22 00:00:00', 'img_1745698491060.png', 1, 'sport', '2025-04-26 21:14:59'),
(110, 'wwwwwwwwww', 'wwwwwwwwwwwww', '2028-04-21 00:00:00', 'img_1745703365845.jpg', 0, 'wwww', '2025-04-26 22:36:49');

-- --------------------------------------------------------

--
-- Structure de la table `commentaire`
--

CREATE TABLE `commentaire` (
  `id` int(11) NOT NULL,
  `activity_id` int(11) NOT NULL,
  `contenu` longtext NOT NULL,
  `note` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `commentaire`
--

INSERT INTO `commentaire` (`id`, `activity_id`, `contenu`, `note`) VALUES
(87, 101, 'hello word', 3),
(88, 86, 'aaaaaaa', 3),
(89, 86, 'aaaaaaaa', 4),
(91, 107, '', 0),
(92, 107, '', 0),
(93, 107, '', 0),
(94, 107, '', 0),
(95, 107, 'bonjour', 2),
(96, 107, 'aaaaaaaa', 2),
(97, 103, 'dfsdf', 2),
(98, 102, 'qsdssq', 4),
(99, 102, 'fgsgf', 3),
(100, 103, 'dsdsq', 4),
(101, 101, 'ththttrh', 4),
(102, 101, 'sgrgerrer', 4);

-- --------------------------------------------------------

--
-- Structure de la table `cours`
--

CREATE TABLE `cours` (
  `id` bigint(20) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `date_heure` datetime NOT NULL,
  `enseignant` varchar(255) NOT NULL,
  `theme` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `devoirs`
--

CREATE TABLE `devoirs` (
  `id` int(11) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `datelimite` datetime NOT NULL,
  `fichier` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `doctrine_migration_versions`
--

CREATE TABLE `doctrine_migration_versions` (
  `version` varchar(191) NOT NULL,
  `executed_at` datetime DEFAULT NULL,
  `execution_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `pack`
--

CREATE TABLE `pack` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `pack`
--

INSERT INTO `pack` (`id`, `name`, `price`, `duration`, `description`) VALUES
(1, 'qqq', 111, 111, 'qqqq');

-- --------------------------------------------------------

--
-- Structure de la table `payment`
--

CREATE TABLE `payment` (
  `id` int(11) NOT NULL,
  `stripe_session_id` varchar(255) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `payment_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `payment_method` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `card_number` varchar(20) DEFAULT NULL,
  `card_expiration` varchar(10) DEFAULT NULL,
  `card_cvv` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `payment`
--

INSERT INTO `payment` (`id`, `stripe_session_id`, `amount`, `payment_date`, `payment_method`, `status`, `card_number`, `card_expiration`, `card_cvv`) VALUES
(1, '', 4445, '2025-04-15 13:45:00', 'hhjh', 'kjjkj', '6555', '685', '6853');

-- --------------------------------------------------------

--
-- Structure de la table `seance_psychologique`
--

CREATE TABLE `seance_psychologique` (
  `id` int(11) NOT NULL,
  `date_seance` date NOT NULL,
  `type_seance` varchar(100) NOT NULL,
  `duree` int(11) NOT NULL,
  `nom_participant` varchar(100) NOT NULL,
  `nom_psychologue` varchar(100) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `soumissiondevoir`
--

CREATE TABLE `soumissiondevoir` (
  `id` int(11) NOT NULL,
  `dateSoumission` datetime NOT NULL,
  `fichier` varchar(255) NOT NULL,
  `note` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `subscription`
--

CREATE TABLE `subscription` (
  `id` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `suivi_psychologique`
--

CREATE TABLE `suivi_psychologique` (
  `id` int(11) NOT NULL,
  `date_suivi` date NOT NULL,
  `suivi_type` varchar(100) NOT NULL,
  `etat_emotionnel` varchar(255) NOT NULL,
  `nom_participant` varchar(100) NOT NULL,
  `nom_psychologue` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `theme`
--

CREATE TABLE `theme` (
  `id` bigint(20) NOT NULL,
  `titre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  `is_verified` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id_utilisateur` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `cin` int(8) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `adresse` varchar(50) NOT NULL,
  `date_naissance` date NOT NULL,
  `telephone` int(8) NOT NULL,
  `type_Utilisateur` enum('admin','etudiant','enseiagnt') NOT NULL,
  `is_verified` tinyint(1) DEFAULT 0,
  `date_creation` timestamp NOT NULL DEFAULT current_timestamp(),
  `photo` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id_utilisateur`, `nom`, `prenom`, `cin`, `email`, `mot_de_passe`, `adresse`, `date_naissance`, `telephone`, `type_Utilisateur`, `is_verified`, `date_creation`, `photo`) VALUES
(1, 'gadour', 'gaodur', 55332255, 'gadour@gmail.com', 'gadour', 'korba', '2000-04-13', 58069420, 'enseiagnt', 1, '2025-04-23 21:07:08', 'uploads\\profils\\user_1_75a65d57-01bc-4bc4-82d8-7c1202db9c3f.png'),
(2, 'aymen', 'aymen', 22335544, 'aymen@gmailc.om', 'aymen', 'korba', '1999-04-23', 58069420, 'etudiant', 0, '2025-04-23 23:25:02', 'uploads\\profils\\user_2_111e8140-4db5-4a4e-b40c-153d8de57fd6.jpg'),
(99, 'admin', 'admin', 0, 'admin@gmail.com', 'admin', 'admin', '2000-04-02', 21365355, 'admin', 1, '2025-04-23 23:27:40', NULL),
(100, 'rayen', 'rayen', 653336633, 'rayen@gmail.com', 'rayen', 'rayen', '1999-04-23', 58695869, 'etudiant', 0, '2025-04-24 00:52:22', 'uploads\\profils\\user_100_be1db691-7a55-4819-8d7a-e7d62dce7277.jpg'),
(101, 'maryem', 'maryeeeem', 55334466, 'maryem@gmail.com', 'maryem', 'korba', '1998-04-17', 99665588, 'enseiagnt', 0, '2025-04-24 08:33:38', 'uploads\\profils\\user_101_3a46a2aa-e85c-4239-bbe9-e303e732f9b6.png'),
(102, 'omar', 'omar', 55223355, 'omar@gmail.com', 'omar', 'nabeul', '1999-04-15', 21356999, 'enseiagnt', 0, '2025-04-24 08:52:15', 'uploads\\profils\\user_102_b3fb7b14-86e6-4bbe-8dc0-9aadccebed52.jpg'),
(103, 'etudiant', 'etudiant', 55555544, 'etudiant@gmail.com', 'etudiant', 'FH', '2000-04-15', 99999999, 'etudiant', 0, '2025-04-26 19:22:25', 'uploads\\profils\\user_103_e410a32d-19b6-45df-b623-9dd9f354dfcb.jpg');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `activity`
--
ALTER TABLE `activity`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_67F068BC81C06096` (`activity_id`);

--
-- Index pour la table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `devoirs`
--
ALTER TABLE `devoirs`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `doctrine_migration_versions`
--
ALTER TABLE `doctrine_migration_versions`
  ADD PRIMARY KEY (`version`);

--
-- Index pour la table `pack`
--
ALTER TABLE `pack`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `seance_psychologique`
--
ALTER TABLE `seance_psychologique`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `soumissiondevoir`
--
ALTER TABLE `soumissiondevoir`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `subscription`
--
ALTER TABLE `subscription`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `suivi_psychologique`
--
ALTER TABLE `suivi_psychologique`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `theme`
--
ALTER TABLE `theme`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id_utilisateur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `activity`
--
ALTER TABLE `activity`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=111;

--
-- AUTO_INCREMENT pour la table `commentaire`
--
ALTER TABLE `commentaire`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=103;

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `devoirs`
--
ALTER TABLE `devoirs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `pack`
--
ALTER TABLE `pack`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `seance_psychologique`
--
ALTER TABLE `seance_psychologique`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `soumissiondevoir`
--
ALTER TABLE `soumissiondevoir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `subscription`
--
ALTER TABLE `subscription`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `suivi_psychologique`
--
ALTER TABLE `suivi_psychologique`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `theme`
--
ALTER TABLE `theme`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id_utilisateur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD CONSTRAINT `FK_67F068BC81C06096` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

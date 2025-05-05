-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 05 mai 2025 à 23:00
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

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
(77, 'aa', 'aa', '2025-04-19 00:00:00', 'C:\\Users\\21658\\Desktop\\plage.png', 1, 'aaa', '2025-04-04 23:16:21'),
(78, 'hhhhhh', 'jhhhhh', '2025-05-10 00:00:00', 'C:\\Users\\21658\\Desktop\\364549317_1301169150494018_440304093175356292_n.jpg', 1, 'hjhjhj', '2025-04-05 00:07:38'),
(79, 'bbbbbbb', 'bbbbbbbbbbbbb', '2025-04-05 19:11:00', '[\"67f1725b303b0.jpg\"]', 0, 'culture', '2025-04-05 18:11:39');

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
(75, 77, 'hello word', 3),
(76, 77, 'hhhhhhhhhhhhhh', 0),
(77, 78, 'somrany aooooooooo y', 2);

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
  `fichier` varchar(255) NOT NULL,
  `idEnseignant` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `devoirs`
--

INSERT INTO `devoirs` (`id`, `titre`, `description`, `datelimite`, `fichier`, `idEnseignant`) VALUES
(45, 'mes etudiants', 'bonjou, mes étudiants je vous aimes beaucoup', '2025-05-11 12:00:00', '1744861209873_1744739918008_resumeFinal-switched-network.pdf', 101),
(46, 'receipt_6', 'azerty', '2025-04-18 12:00:00', '1744883787561_receipt_6.pdf', 101),
(47, 'resumeFinal-switched-network (1)', 'zertyu', '2025-04-18 12:00:00', '1744917213329_resumeFinal-switched-network (1).pdf', NULL),
(50, 'resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaaaa', '2025-05-02 12:00:00', '1745718896424_resumeFinal-switched-network (1).pdf', 101),
(52, 'receipt_4', 'cccccccccccccccccccccc', '2025-05-02 12:00:00', '1745718944602_receipt_4.pdf', 101),
(53, 'resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaaaa', '2025-05-09 12:00:00', '1745719114709_resumeFinal-switched-network (1).pdf', 99),
(54, 'receipt_4', 'aaaaaaaaaaaaaaaaaaaaaaa', '2025-04-30 12:00:00', '1745719132170_receipt_4.pdf', 99),
(55, 'resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaa', '2025-05-08 12:00:00', '1745719149094_resumeFinal-switched-network (1).pdf', 99),
(56, 'resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaaaaaaaaaaa', '2025-05-02 12:00:00', '1745719168054_resumeFinal-switched-network (1).pdf', 99),
(57, 'receipt_4', 'aaaaaaaaaaaaaaaaaaaaaaaaa', '2025-05-07 12:00:00', '1745719187352_receipt_4.pdf', 99),
(58, '1744647259307_resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaaaa', '2025-05-02 12:00:00', '1745719207082_1744647259307_resumeFinal-switched-network (1).pdf', 99),
(59, 'resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', '2025-04-30 12:00:00', '1745719225535_resumeFinal-switched-network (1).pdf', 99),
(60, 'L222 (5) (1)', 'aaaaaaaaaaaaaaaaaaaaa', '2025-05-10 12:00:00', '1745719245186_L222 (5) (1).pdf', 99),
(61, 'receipt_4', 'aaaaaaaaaaaaaaaaaaaaaaaaaa', '2025-05-11 12:00:00', '1745719264259_receipt_4.pdf', 99),
(62, 'receipt_4', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', '2025-05-11 12:00:00', '1745719282163_receipt_4.pdf', 99),
(63, '1744647259307_resumeFinal-switched-network (1)', 'kkkkkkkkkkkkkkkkkkkkkk', '2025-05-09 12:00:00', '1745719547132_1744647259307_resumeFinal-switched-network (1).pdf', 200),
(64, '1744647259307_resumeFinal-switched-network (1)', 'sssssssssssssssss', '2025-05-08 12:00:00', '1745719563293_1744647259307_resumeFinal-switched-network (1).pdf', 200),
(65, 'resumeFinal-switched-network (1)', 'ssssssssssssssssssssssssss', '2025-05-03 12:00:00', '1745719581160_resumeFinal-switched-network (1).pdf', 200),
(66, 'resumeFinal-switched-network (1)', 'ssssssssssssssssssssssssssssssssssssssssss', '2025-05-02 12:00:00', '1745719595785_resumeFinal-switched-network (1).pdf', 200),
(67, '1744739918008_resumeFinal-switched-network', 'kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk', '2025-05-03 12:00:00', '1745719610464_1744739918008_resumeFinal-switched-network.pdf', 200),
(68, 'bbbbbbbbbb', 'bbbbbbbb', '2025-05-03 12:00:00', '1745778274222_resumeFinal-switched-network (1).pdf', 101),
(70, 'resumeFinal-switched-network (1)', 'azertyui', '2025-05-01 12:00:00', '1745873130154_resumeFinal-switched-network (1).pdf', 101),
(71, '1744647259307_resumeFinal-switched-network (1)', 'aaaaaaaaaaaaaaaaaaa', '2025-05-03 12:00:00', '1745873866506_1744647259307_resumeFinal-switched-network (1).pdf', 101),
(72, '1744647259307_resumeFinal-switched-network (1)', 'aymen somrani', '2025-05-03 12:00:00', '1745935370315_1744647259307_resumeFinal-switched-network (1).pdf', 101),
(73, 'NOURANE', 'AAAAAAAAAAAAAAAAAAAA', '2025-04-30 12:00:00', '1745939973785_resumeFinal-switched-network (1).pdf', 101),
(74, '1744739918008_resumeFinal-switched-network', 'bonjour mon ami achref', '2025-05-02 12:00:00', '1745949973224_1744739918008_resumeFinal-switched-network.pdf', 101),
(75, 'resumeFinal', 'azert', '2025-05-06 12:00:00', '1746022672133_resumeFinal-switched-network (1).pdf', 101),
(77, '1744647259307_resumeFinal-switched-network (1)', 'tester mailing dans l\'integration', '2025-05-16 12:00:00', '1746461829692_1744647259307_resumeFinal-switched-network (1).pdf', 101),
(78, '1744647259307_resumeFinal-switched-network (1)', 'ziferiuggijehiohgoehg', '2025-05-16 12:00:00', '1746469762609_1744647259307_resumeFinal-switched-network (1).pdf', 101);

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
  `note` int(11) DEFAULT NULL,
  `devoir_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `soumissiondevoir`
--

INSERT INTO `soumissiondevoir` (`id`, `dateSoumission`, `fichier`, `note`, `devoir_id`) VALUES
(11, '2029-04-06 19:19:50', 'soumission_1744878743933_resumeFinal-switched-network (1).pdf', 15, 45),
(13, '2025-04-17 20:12:01', 'soumission_1744917121959_1744647259307_resumeFinal-switched-network (1).pdf', 18, 46),
(17, '2025-04-28 15:05:33', 'C:\\Users\\aymen\\Downloads\\1744647259307_resumeFinal-switched-network (1).pdf', NULL, 56),
(19, '2025-04-29 16:21:47', 'soumission_1745940107310_1744647259307_resumeFinal-switched-network (1).pdf', 20, 73),
(20, '2025-05-05 17:34:01', 'soumission_77_1746462841178.pdf', NULL, 77),
(21, '2025-05-05 18:46:37', 'soumission_45_1746467197160.pdf', NULL, 45);

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
  `photo` text DEFAULT NULL,
  `id_enseignant` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id_utilisateur`, `nom`, `prenom`, `cin`, `email`, `mot_de_passe`, `adresse`, `date_naissance`, `telephone`, `type_Utilisateur`, `is_verified`, `date_creation`, `photo`, `id_enseignant`) VALUES
(1, 'gadour', 'gaodur', 55332255, 'gadour@gmail.com', 'gadour', 'korba', '2000-04-13', 58069420, 'admin', 1, '2025-04-23 20:07:08', 'uploads\\profils\\user_1_75a65d57-01bc-4bc4-82d8-7c1202db9c3f.png', NULL),
(2, 'aymen', 'aymen', 22335544, 'aymen@gmailc.om', 'aymen', 'korba', '1999-04-23', 58069420, 'etudiant', 0, '2025-04-23 22:25:02', 'uploads\\profils\\user_2_111e8140-4db5-4a4e-b40c-153d8de57fd6.jpg', NULL),
(99, 'admin', 'admin', 0, 'admin@gmail.com', 'admin', 'admin', '2000-04-02', 21365355, 'enseiagnt', 1, '2025-04-23 22:27:40', NULL, NULL),
(100, 'rayen', 'rayen', 653336633, 'rayen@gmail.com', 'rayen', 'rayen', '1999-04-23', 58695869, 'etudiant', 1, '2025-04-23 23:52:22', 'uploads\\profils\\user_100_be1db691-7a55-4819-8d7a-e7d62dce7277.jpg', NULL),
(101, 'zina', 'somrani', 9947721, 'zina@gmail.com', '09947721', 'siliana', '2000-03-31', 99116165, 'enseiagnt', 1, '2025-04-25 01:34:46', 'uploads\\profils\\user_101_4c0207c8-ea84-4f15-bfba-ac964d9cb5fd.JPG', NULL),
(200, 'mohamed', 'somrani', 9947721, 'mohamed@gmail.com', '123456789', 'siliana', '2025-04-15', 99988852, 'enseiagnt', 1, '2025-04-16 02:02:25', NULL, NULL);

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
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_enseignant_id` (`idEnseignant`);

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
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_devoir_id` (`devoir_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=80;

--
-- AUTO_INCREMENT pour la table `commentaire`
--
ALTER TABLE `commentaire`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `devoirs`
--
ALTER TABLE `devoirs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- AUTO_INCREMENT pour la table `pack`
--
ALTER TABLE `pack`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `seance_psychologique`
--
ALTER TABLE `seance_psychologique`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `soumissiondevoir`
--
ALTER TABLE `soumissiondevoir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

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
  MODIFY `id_utilisateur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=201;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD CONSTRAINT `FK_67F068BC81C06096` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `devoirs`
--
ALTER TABLE `devoirs`
  ADD CONSTRAINT `fk_enseignant_id` FOREIGN KEY (`idEnseignant`) REFERENCES `utilisateur` (`id_utilisateur`);

--
-- Contraintes pour la table `soumissiondevoir`
--
ALTER TABLE `soumissiondevoir`
  ADD CONSTRAINT `fk_devoir_id` FOREIGN KEY (`devoir_id`) REFERENCES `devoirs` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

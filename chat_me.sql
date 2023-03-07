-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2022 at 02:25 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chat_me`
--

-- --------------------------------------------------------

--
-- Table structure for table `chats`
--

CREATE TABLE `chats` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id1` bigint(20) UNSIGNED NOT NULL,
  `user_id2` bigint(20) UNSIGNED NOT NULL,
  `type` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `chats`
--

INSERT INTO `chats` (`id`, `user_id1`, `user_id2`, `type`, `name`, `content`, `date`) VALUES
(98, 15, 16, 1, NULL, 'a', NULL),
(99, 15, 16, 3, 'Screenshot_20221206_114754.png', 'C:\\Users\\Admin\\Pictures\\Screenshots\\Screenshot_20221206_114754.png', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `chat_group`
--

CREATE TABLE `chat_group` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `type` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `chat_group`
--

INSERT INTO `chat_group` (`id`, `user_id`, `type`, `name`, `content`, `date`) VALUES
(9, 16, 1, NULL, 'ab', NULL),
(10, 16, 1, NULL, 'babaab', NULL),
(11, 15, 1, NULL, 'Hello', NULL),
(12, 18, 1, NULL, 'Hello', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `user_1` bigint(20) UNSIGNED NOT NULL,
  `user_2` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `friends`
--

INSERT INTO `friends` (`user_1`, `user_2`) VALUES
(16, 15),
(18, 15),
(18, 15),
(18, 15);

-- --------------------------------------------------------

--
-- Table structure for table `request_friends`
--

CREATE TABLE `request_friends` (
  `user_1` bigint(20) UNSIGNED NOT NULL,
  `user_2` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `username` varchar(255) NOT NULL,
  `avatar` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `avatar`, `password`, `email`, `phone`, `gender`) VALUES
(15, 'a', 'https://res.cloudinary.com/diw0u2vl1/image/upload/v1641639419/default_avatar/mnuue0txf2qv1zhpsbno.jpg', '1', 'a', 'a', 'male'),
(16, 'b', 'https://res.cloudinary.com/diw0u2vl1/image/upload/v1641639419/default_avatar/mnuue0txf2qv1zhpsbno.jpg', '1', 'b', 'b', 'male'),
(17, '', 'https://res.cloudinary.com/diw0u2vl1/image/upload/v1641639419/default_avatar/mnuue0txf2qv1zhpsbno.jpg', '', '', '', 'male'),
(18, 'c', 'https://res.cloudinary.com/diw0u2vl1/image/upload/v1641639419/default_avatar/mnuue0txf2qv1zhpsbno.jpg', '1', '1', '1', 'male');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chats`
--
ALTER TABLE `chats`
  ADD PRIMARY KEY (`id`),
  ADD KEY `send` (`user_id1`),
  ADD KEY `recv` (`user_id2`);

--
-- Indexes for table `chat_group`
--
ALTER TABLE `chat_group`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chat_group` (`user_id`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD KEY `user1` (`user_1`),
  ADD KEY `user2` (`user_2`);

--
-- Indexes for table `request_friends`
--
ALTER TABLE `request_friends`
  ADD KEY `request_user1` (`user_1`),
  ADD KEY `request_user2` (`user_2`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chats`
--
ALTER TABLE `chats`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `chat_group`
--
ALTER TABLE `chat_group`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chats`
--
ALTER TABLE `chats`
  ADD CONSTRAINT `recv` FOREIGN KEY (`user_id2`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `send` FOREIGN KEY (`user_id1`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `chat_group`
--
ALTER TABLE `chat_group`
  ADD CONSTRAINT `chat_group` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `friends`
--
ALTER TABLE `friends`
  ADD CONSTRAINT `user1` FOREIGN KEY (`user_1`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user2` FOREIGN KEY (`user_2`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `request_friends`
--
ALTER TABLE `request_friends`
  ADD CONSTRAINT `request_user1` FOREIGN KEY (`user_1`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `request_user2` FOREIGN KEY (`user_2`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

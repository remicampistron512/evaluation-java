-- --------------------------------------------------------
-- Hôte:                         127.0.0.1
-- Version du serveur:           12.1.2-MariaDB - MariaDB Server
-- SE du serveur:                Win64
-- HeidiSQL Version:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Listage de la structure de la base pour courses_shop
CREATE DATABASE IF NOT EXISTS `courses_shop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `courses_shop`;

-- Listage de la structure de table courses_shop. attendance_mode
CREATE TABLE IF NOT EXISTS `attendance_mode` (
  `code` varchar(10) NOT NULL,
  `label` varchar(50) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `1` FOREIGN KEY (`user_id`) REFERENCES `user_` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. cart_item
CREATE TABLE IF NOT EXISTS `cart_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cart_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cart_id` (`cart_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `1` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. course
CREATE TABLE IF NOT EXISTS `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` text NOT NULL,
  `duration_days` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `attendance_mode_code` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_course_attendance_mode` (`attendance_mode_code`),
  CONSTRAINT `FK_course_attendance_mode` FOREIGN KEY (`attendance_mode_code`) REFERENCES `attendance_mode` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. course_category
CREATE TABLE IF NOT EXISTS `course_category` (
  `course_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`course_id`,`category_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. customer
CREATE TABLE IF NOT EXISTS `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(190) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(40) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. line_order
CREATE TABLE IF NOT EXISTS `line_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `1` FOREIGN KEY (`order_id`) REFERENCES `order_` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. order_
CREATE TABLE IF NOT EXISTS `order_` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `status_code` varchar(12) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL DEFAULT 0.00,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `customer_id` (`customer_id`),
  KEY `status_code` (`status_code`),
  CONSTRAINT `1` FOREIGN KEY (`user_id`) REFERENCES `user_` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `3` FOREIGN KEY (`status_code`) REFERENCES `order_status` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. order_status
CREATE TABLE IF NOT EXISTS `order_status` (
  `code` varchar(12) NOT NULL,
  `label` varchar(50) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

-- Listage de la structure de table courses_shop. user_
CREATE TABLE IF NOT EXISTS `user_` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `login` varchar(80) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Les données exportées n'étaient pas sélectionnées.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

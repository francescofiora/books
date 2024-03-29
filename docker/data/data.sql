SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `books` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `books`;

CREATE USER 'japp'@'%' IDENTIFIED BY 'secret';
GRANT SELECT, INSERT, UPDATE, DELETE ON `books`.* TO 'japp'@'%';

CREATE USER 'jadmin'@'%' IDENTIFIED BY 'moresecret';
GRANT ALL PRIVILEGES ON `books`.* TO 'jadmin'@'%';
FLUSH PRIVILEGES;

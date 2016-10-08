-- Video Record Management System for Presentation Assessment
-- Database Schema for MySQL version 5.6.5 or above
--
-- MySQL version 5.6.5 or above is needed for automatic initialization and updating for DATETIME fields
-- see http://dev.mysql.com/doc/refman/5.6/en/timestamp-initialization.html
--
-- InnoDB is used because it provides "transaction", "foreign key" and row-level lock, and no spatial index is needed
-- see http://dev.mysql.com/doc/refman/5.6/en/innodb-introduction.html
--
-- The Department of Electronic and Information Engineering
-- The Hong Kong Polytechnic University
-- 2014-2016 (c) All Rights Reserved

CREATE DATABASE IF NOT EXISTS `ePre` DEFAULT CHARACTER SET=utf8mb4;

USE `ePre`;

CREATE TABLE IF NOT EXISTS `t_department` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`abbreviation` VARCHAR(8) NOT NULL,
	`name` VARCHAR(64) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY (`abbreviation`),
	UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_user` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_name` VARCHAR(20) NOT NULL,
	`name` VARCHAR(64) NOT NULL,
	`department_id` INT UNSIGNED NOT NULL,
	`category` TINYINT NOT NULL COMMENT "0: admin; 1: student; 2: teacher; 3: outsider",
	PRIMARY KEY (`id`),
	UNIQUE KEY (`user_name`),
	FOREIGN KEY (`department_id`) REFERENCES `t_department`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_user_password` (
	`user_id` INT UNSIGNED NOT NULL,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`password` CHAR(64) NOT NULL COMMENT "SHA-256 of a user's password",
	PRIMARY KEY (`user_id`),
	FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_presentation` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`department_id` INT UNSIGNED NOT NULL,
	`name` VARCHAR(32) NOT NULL,
	`semester` CHAR(5) NOT NULL COMMENT "2015a: 2015-2016 all semester; 2016b: 2016-2017 semester 1 and 2; 20141: 2014-2015 semester 1",
	PRIMARY KEY (`id`),
	UNIQUE KEY `presentation_name` (`semester`, `department_id`, `name`),
	FOREIGN KEY (`department_id`) REFERENCES `t_department`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_video` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`presentation_id` INT UNSIGNED NOT NULL,
	`name` VARCHAR(32) NOT NULL,
	`info` VARCHAR(256),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`presentation_id`) REFERENCES `t_presentation`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_assessment` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`video_id` INT UNSIGNED NOT NULL,
	`permission` TINYINT COMMENT "0: view only; 1: comment; 2: comment and grade",
	`grade` TINYINT UNSIGNED,
	`grade_weight` TINYINT UNSIGNED COMMENT "In percentage, 0~100",
	PRIMARY KEY (`id`),
	UNIQUE KEY `video_assessor` (`user_id`, `video_id`),
	FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`video_id`) REFERENCES `t_video`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT "The information of which teacher can assess which video";

CREATE TABLE IF NOT EXISTS `t_comment` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`video_id` INT UNSIGNED NOT NULL,
	`playtime` FLOAT NOT NULL,
	`content` VARCHAR(4096),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`video_id`) REFERENCES `t_video`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_response` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`comment_id` INT UNSIGNED NOT NULL,
	`content` VARCHAR(4096),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`comment_id`) REFERENCES `t_comment`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT 'Responses to the comment';

CREATE TABLE IF NOT EXISTS `t_message` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`from_user_id` INT UNSIGNED NOT NULL,
	`to_user_id` INT UNSIGNED COMMENT 'null: to all users (system announcement)',
	`title` VARCHAR(64),
	`content` VARCHAR(4096),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`from_user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`to_user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

INSERT INTO `t_department` (`id`, `abbreviation`, `name`) VALUES
	(1, "ABCT", "Applied Biology and Chemical Technology"),
	(2, "AF",   "Accounting and Finance"),
	(3, "AMA",  "Applied Mathematics"),
	(4, "AP",   "Applied Physics"),
	(5, "APSS", "Applied Social Sciences"),
	(6, "BME",  "Biomedical Engineering"),
	(7, "BRE",  "Building and Real Estate"),
	(8, "BSE",  "Building Services Engineering"),
	(9, "CBS",  "Chinese and Bilingual Studies"),
	(10, "CC",   "Chinese Culture"),
	(11, "CEE",  "Civil and Environmental Engineering"),
	(12, "COMP", "Computing"),
	(13, "EE",   "Electrical Engineering"),
	(14, "EIE",  "Electronic and Information Engineering"),
	(15, "ELC",  "English Language Centre"),
	(16, "ENGL", "English"),
	(17, "GEC",  "General Education Centre"),
	(18, "HTI",  "Health Technology and Informatics"),
	(19, "ISE",  "Industrial and Systems Engineering"),
	(20, "ITC",  "Textiles and Clothing"),
	(21, "LMS",  "Logistics and Maritime Studies"),
	(22, "LSGI", "Land Surveying and Geo-Informatics"),
	(23, "ME",   "Mechanical Engineering"),
	(24, "MM",   "Management and Marketing"),
	(25, "RS",   "Rehabilitation Sciences"),
	(26, "SD",   "School of Design"),
	(27, "SHTM", "Hotel and Tourism Management"),
	(28, "SN",   "School of Nursing"),
	(29, "SO",   "School of Optometry");

INSERT INTO `t_user` (`id`, `user_name`, `name`, `department_id`, `category`) VALUES
	(1, "admin", "Administrator", 14, 0),
	(2, "13116487d", "Wu Yuping", 14, 1),
	(3, "enchikin", "Dr. Leung Chi-Kin", 14, 2);

INSERT INTO `t_user_password` (`user_id`, `password`) VALUES
	(1, sha2("FYPfyp", 256)),
	(2, sha2("13116487d", 256)),
	(3, sha2("enchikin", 256));

INSERT INTO `t_message` (`from_user_id`, `title`, `content`) VALUES
	(1, "Annoucement1", "Hello User! Welcome to the ePre system"),
	(1, "Annoucement2", "The system is under developmet"),
	(1, "Annoucement3", "Test Annoucement Test Annoucement");

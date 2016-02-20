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

CREATE DATABASE IF NOT EXISTS `ePre` DEFAULT CHARACTER SET=utf8;

USE `ePre`;

CREATE TABLE IF NOT EXISTS `Departments` (
	`id` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`abbreviation` VARCHAR(8) NOT NULL,
	`name` VARCHAR(64) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY (`abbreviation`),
	UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE IF NOT EXISTS `Accounts` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` VARCHAR(20) NOT NULL,
	`password` CHAR(64) NOT NULL COMMENT "SHA-256 of a user's password",
	`name` VARCHAR(64) NOT NULL,
	`department_id` TINYINT UNSIGNED NOT NULL,
	`category` TINYINT NOT NULL COMMENT "0: admin; 1: student; 2: teacher; 3: outsider",
	PRIMARY KEY (`id`),
	UNIQUE KEY (`user_id`),
	FOREIGN KEY (`department_id`) REFERENCES `Departments`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE IF NOT EXISTS `Presentations` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`department_id` TINYINT UNSIGNED NOT NULL,
	`name` VARCHAR(32) NOT NULL,
	`semester` CHAR(5) NOT NULL COMMENT "2015a: 2015-2016 all semester; 2016b: 2016-2017 semester 1 and 2; 20141: 2014-2015 semester 1",
	PRIMARY KEY (`id`),
	UNIQUE KEY `presentation_name` (`semester`, `department_id`, `name`),
	FOREIGN KEY (`department_id`) REFERENCES `Departments`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE IF NOT EXISTS `Videos` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`presentation_id` INT UNSIGNED NOT NULL,
	`name` VARCHAR(32) NOT NULL,
	`info` VARCHAR(256),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `Accounts`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`presentation_id`) REFERENCES `Presentations`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE IF NOT EXISTS `Assessments` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`video_id` INT UNSIGNED NOT NULL,
	`permission` TINYINT COMMENT "0: view only; 1: comment; 2: comment and grade",
	`grade` TINYINT,
	PRIMARY KEY (`id`),
	UNIQUE KEY `video_assessor` (`user_id`, `video_id`),
	FOREIGN KEY (`user_id`) REFERENCES `Accounts`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`video_id`) REFERENCES `Videos`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COMMENT "The information of which teacher can assess which video";

CREATE TABLE IF NOT EXISTS `Comments` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`video_id` INT UNSIGNED NOT NULL,
	`playtime` FLOAT NOT NULL,
	`content` VARCHAR(4096),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `Accounts`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`video_id`) REFERENCES `Videos`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE IF NOT EXISTS `Responses` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_id` INT UNSIGNED NOT NULL,
	`comment_id` INT UNSIGNED NOT NULL,
	`content` VARCHAR(4096),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `Accounts`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`comment_id`) REFERENCES `Comments`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE IF NOT EXISTS `Messages` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`from_user_id` INT UNSIGNED NOT NULL,
	`to_user_id` INT UNSIGNED,
	`title` VARCHAR(64),
	`content` VARCHAR(4096),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`from_user_id`) REFERENCES `Accounts`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`to_user_id`) REFERENCES `Accounts`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

INSERT INTO `Departments` (`abbreviation`, `name`) VALUES
	("ABCT", "Applied Biology and Chemical Technology"),
	("AF",   "Accounting and Finance"),
	("AMA",  "Applied Mathematics"),
	("AP",   "Applied Physics"),
	("APSS", "Applied Social Sciences"),
	("BME",  "Biomedical Engineering"),
	("BRE",  "Building and Real Estate"),
	("BSE",  "Building Services Engineering"),
	("CBS",  "Chinese and Bilingual Studies"),
	("CC",   "Chinese Culture"),
	("CEE",  "Civil and Environmental Engineering"),
	("COMP", "Computing"),
	("EE",   "Electrical Engineering"),
	("EIE",  "Electronic and Information Engineering"),
	("ELC",  "English Language Centre"),
	("ENGL", "English"),
	("GEC",  "General Education Centre"),
	("HTI",  "Health Technology and Informatics"),
	("ISE",  "Industrial and Systems Engineering"),
	("ITC",  "Textiles and Clothing"),
	("LMS",  "Logistics and Maritime Studies"),
	("LSGI", "Land Surveying and Geo-Informatics"),
	("ME",   "Mechanical Engineering"),
	("MM",   "Management and Marketing"),
	("RS",   "Rehabilitation Sciences"),
	("SD",   "School of Design"),
	("SHTM", "Hotel and Tourism Management"),
	("SN",   "School of Nursing"),
	("SO",   "School of Optometry");

INSERT INTO `Accounts` (`user_id`, `name`, `password`, `department_id`, `category`) VALUES
	("admin", "Administrator", sha2("FYPfyp", 256), 14, 0),
	("13116487d", "Wu Yuping", sha2("13116487d", 256), 14, 1),
	("14122983d","Liu Lu", sha2("14122983d", 256), 14, 1),
	("enchikin", "Dr. Leung Chi-Kin", sha2("enchikin", 256), 14, 2);

INSERT INTO `Messages` (`from_user_id`, `title`, `content`) VALUES
	(1, "Annoucement1", "Hello User! Welcome to the ePre system"),
	(1, "Annoucement2", "The system is under developmet"),
	(1, "Annoucement3", "Test Annoucement Test Annoucement");
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema jb_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema jb_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jb_db` DEFAULT CHARACTER SET utf8 ;
USE `jb_db` ;

-- -----------------------------------------------------
-- Table `jb_db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `age` INT NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL COMMENT 'Hashing',
  `gender` ENUM('Male', 'Female', 'Other') NOT NULL DEFAULT 'Other',
  `is_street_pass` TINYINT NOT NULL DEFAULT 0,
  `img_url` VARCHAR(255) NOT NULL,
  `favorite_music` VARCHAR(50) NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`providers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`providers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`user_providers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`user_providers` (
  `user_id` INT NOT NULL,
  `provider_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL COMMENT 'Hashing',
  PRIMARY KEY (`user_id`, `provider_id`),
  INDEX `fk_user_providers_providers1_idx` (`provider_id` ASC) INVISIBLE,
  CONSTRAINT `fk_user_providers_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_providers_providers1`
    FOREIGN KEY (`provider_id`)
    REFERENCES `jb_db`.`providers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`friends`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`friends` (
  `user1_id` INT NOT NULL,
  `user2_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user1_id`, `user2_id`),
  INDEX `fk_friends_users2_idx` (`user2_id` ASC) VISIBLE,
  CONSTRAINT `fk_friends_users1`
    FOREIGN KEY (`user1_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_friends_users2`
    FOREIGN KEY (`user2_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `CK_friends_user_order` CHECK (user1_id < user2_id))
	
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`friend_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`friend_requests` (
  `send_user_id` INT NOT NULL,
  `pass_user_id` INT NOT NULL,
  `state` ENUM('pending', 'accepted', 'denied') NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`send_user_id`, `pass_user_id`),
  INDEX `fk_friend_requests_users2_idx` (`pass_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_friend_requests_users1`
    FOREIGN KEY (`send_user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_friend_requests_users2`
    FOREIGN KEY (`pass_user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`scenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`scenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`session_sort_settings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`session_sort_settings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(50) NOT NULL,
  `description` TEXT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`sessions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`sessions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL COMMENT 'Hashing',
  `user_id` INT NOT NULL,
  `provider_id` INT NOT NULL,
  `scene_id` INT NOT NULL,
  `default_sort_id` INT NOT NULL,
  `description` TEXT NULL,
  `img_url` VARCHAR(255) NOT NULL,
  `finished` TINYINT NOT NULL DEFAULT 0,
  `finished_at` DATETIME NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_sessions_users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_sessions_providers1_idx` (`provider_id` ASC) VISIBLE,
  INDEX `fk_sessions_scenes1_idx` (`scene_id` ASC) VISIBLE,
  INDEX `fk_sessions_session_sort_settings1_idx` (`default_sort_id` ASC) VISIBLE,
  CONSTRAINT `fk_sessions_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sessions_providers1`
    FOREIGN KEY (`provider_id`)
    REFERENCES `jb_db`.`providers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sessions_scenes1`
    FOREIGN KEY (`scene_id`)
    REFERENCES `jb_db`.`scenes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sessions_session_sort_settings1`
    FOREIGN KEY (`default_sort_id`)
    REFERENCES `jb_db`.`session_sort_settings` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`session_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`session_tags` (
  `session_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`session_id`, `tag_id`),
  INDEX `fk_session_tags_tags1_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_session_tags_sessions1`
    FOREIGN KEY (`session_id`)
    REFERENCES `jb_db`.`sessions` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_session_tags_tags1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `jb_db`.`tags` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`guests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`guests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NULL,
  `user_id` INT NULL,
  `session_id` INT NOT NULL,
  `authority` ENUM('Guest', 'Editor', 'Maintainer', 'Manager', 'Admin', 'Banned') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_guests_users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_guests_sessions1_idx` (`session_id` ASC) VISIBLE,
  CONSTRAINT `fk_guests_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_guests_sessions1`
    FOREIGN KEY (`session_id`)
    REFERENCES `jb_db`.`sessions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`requests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `session_id` INT NOT NULL,
  `guest_id` INT NOT NULL,
  `music_id` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_requests_sessions1_idx` (`session_id` ASC) VISIBLE,
  INDEX `fk_requests_guests1_idx` (`guest_id` ASC) VISIBLE,
  CONSTRAINT `fk_requests_sessions1`
    FOREIGN KEY (`session_id`)
    REFERENCES `jb_db`.`sessions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_requests_guests1`
    FOREIGN KEY (`guest_id`)
    REFERENCES `jb_db`.`guests` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`request_caches`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`request_caches` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `session_id` INT NOT NULL,
  `guest_id` INT NOT NULL,
  `music_id` VARCHAR(50) NOT NULL,
  `order_index` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_request_caches_sessions1_idx` (`session_id` ASC) VISIBLE,
  INDEX `fk_request_caches_guests1_idx` (`guest_id` ASC) VISIBLE,
  CONSTRAINT `fk_request_caches_sessions1`
    FOREIGN KEY (`session_id`)
    REFERENCES `jb_db`.`sessions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_request_caches_guests1`
    FOREIGN KEY (`guest_id`)
    REFERENCES `jb_db`.`guests` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`street_pass_options`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`street_pass_options` (
  `user_id` INT NOT NULL,
  `playlist_endpoint` VARCHAR(255) NULL,
  `message` TEXT NULL,
  `secret_mode` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_street_pass_options_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`street_pass_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`street_pass_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `passed_user1_id` INT NOT NULL,
  `passed_user2_id` INT NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_street_pass_history_users1_idx` (`passed_user1_id` ASC) VISIBLE,
  INDEX `fk_street_pass_history_users2_idx` (`passed_user2_id` ASC) VISIBLE,
  CONSTRAINT `fk_street_pass_history_users1`
    FOREIGN KEY (`passed_user1_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_street_pass_history_users2`
    FOREIGN KEY (`passed_user2_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`fornows`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`fornows` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `music_id` VARCHAR(50) NOT NULL,
  `message` TEXT NULL,
  `finished` TINYINT NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_fornows_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_fornows_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`fornow_likes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`fornow_likes` (
  `fornow_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`fornow_id`, `user_id`),
  INDEX `fk_fornow_likes_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_fornow_likes_fornows1`
    FOREIGN KEY (`fornow_id`)
    REFERENCES `jb_db`.`fornows` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_fornow_likes_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jb_db`.`messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jb_db`.`messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `content` TEXT NOT NULL,
  `is_read` TINYINT NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_messages_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_messages_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jb_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

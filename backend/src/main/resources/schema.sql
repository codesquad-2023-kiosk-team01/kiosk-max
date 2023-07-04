-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema kiosk
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema kiosk
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `kiosk` DEFAULT CHARACTER SET utf8;
USE `kiosk`;

-- -----------------------------------------------------
-- Table `kiosk`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`category`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`item`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45)  NOT NULL,
    `price`       INT          NOT NULL,
    `image`       VARCHAR(512) NOT NULL,
    `description` VARCHAR(128) NOT NULL,
    `category_id` INT          NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_item_category_idx` (`category_id` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`payment`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(45)  NOT NULL,
    `image` VARCHAR(512) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`orders`
(
    `id`         INT      NOT NULL AUTO_INCREMENT,
    `amount`     INT      NOT NULL COMMENT '지불금액',
    `total`      INT      NOT NULL COMMENT '총 결제금액',
    `remain`     INT      NOT NULL COMMENT '거스름돈',
    `order_date` DATETIME NOT NULL,
    `order_num`  INT      NOT NULL,
    `payment_id` INT      NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_orders_payment1_idx` (`payment_id` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`order_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`order_item`
(
    `id`            INT NOT NULL AUTO_INCREMENT,
    `item_quantity` INT NOT NULL,
    `price`         INT NOT NULL,
    `item_id`       INT NOT NULL,
    `orders_id`     INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_order_item_item1_idx` (`item_id` ASC) VISIBLE,
    INDEX `fk_order_item_orders1_idx` (`orders_id` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`option_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`option_type`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`options`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`options`
(
    `id`             INT         NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(45) NOT NULL,
    `option_type_id` INT         NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_option_option_type1_idx` (`option_type_id` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`order_item_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`order_item_option`
(
    `id`            INT NOT NULL AUTO_INCREMENT,
    `options_id`    INT NOT NULL,
    `order_item_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_order_item_option_option1_idx` (`options_id` ASC) VISIBLE,
    INDEX `fk_order_item_option_order_item1_idx` (`order_item_id` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kiosk`.`item_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kiosk`.`item_option`
(
    `id`         INT NOT NULL AUTO_INCREMENT,
    `options_id` INT NOT NULL,
    `item_id`    INT NOT NULL,
    INDEX `fk_option_has_item_option1_idx` (`options_id` ASC) VISIBLE,
    INDEX `fk_option_has_item_item1_idx` (`item_id` ASC) VISIBLE,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

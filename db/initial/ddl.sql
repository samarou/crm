USE security_sample;


DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `acl_entry`;
DROP TABLE IF EXISTS `acl_object_identity`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `user_group`;
DROP TABLE IF EXISTS `role_privilege`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `group`;
DROP TABLE IF EXISTS `principal`;
DROP TABLE IF EXISTS `privilege`;
DROP TABLE IF EXISTS `object_type`;
DROP TABLE IF EXISTS `action`;

DROP TRIGGER IF EXISTS `trg_user_adr`;
DROP TRIGGER IF EXISTS `trg_group_adr`;


-- COMMON TABLES -------------------------------------------------------------------

CREATE TABLE `action` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(20) NOT NULL,
	`description` VARCHAR(250),
	UNIQUE KEY `uk_action_name`(`name`)
) ENGINE=InnoDB;


CREATE TABLE `object_type` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(100) NOT NULL,
	`description` VARCHAR(250),
	UNIQUE KEY `uk_object_type_name`(`name`)
) ENGINE=InnoDB;


CREATE TABLE `privilege` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`object_type_id` BIGINT UNSIGNED NOT NULL,
	`action_id` BIGINT UNSIGNED NOT NULL,
    UNIQUE KEY `uk_privilege_obj_type_action` (`object_type_id`, `action_id`),
	CONSTRAINT `fk_privilege_object_type_id` FOREIGN KEY (`object_type_id`) REFERENCES `object_type` (`id`),
	CONSTRAINT `fk_privilege_action` FOREIGN KEY (`action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `role` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(250),
	`parent_id` BIGINT UNSIGNED,
    UNIQUE KEY `uk_role_name` (`name`),
	CONSTRAINT `fk_role_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `principal` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB;


CREATE TABLE `group` (
	`id` BIGINT UNSIGNED NOT NULL PRIMARY KEY,
	`name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(250),
    UNIQUE KEY `uk_group_name` (`name`),
    CONSTRAINT `fk_group_principle_id` FOREIGN KEY (`id`) REFERENCES `principal` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `user` (
	`id` BIGINT UNSIGNED NOT NULL PRIMARY KEY,
	`user_name` VARCHAR(50) NOT NULL,
	`password` VARCHAR(60) NOT NULL,
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY `uk_user_user_name` (`user_name`),
    CONSTRAINT `fk_user_principle_id` FOREIGN KEY (`id`) REFERENCES `principal` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `user_role` (
	`user_id` BIGINT UNSIGNED NOT NULL,
	`role_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
	CONSTRAINT `fk_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `fk_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `user_group` (
	`user_id` BIGINT UNSIGNED NOT NULL,
	`group_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `group_id`),
	CONSTRAINT `fk_user_group_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `fk_user_group_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `role_privilege` (
	`role_id` BIGINT UNSIGNED NOT NULL,
	`privilege_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`role_id`, `privilege_id`),
	CONSTRAINT `fk_role_privilege_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
	CONSTRAINT `fk_role_privilege_privilege_id` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `customer` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `firstName` VARCHAR(50) NOT NULL,
	`lastName` VARCHAR(50) NOT NULL,
	`email` VARCHAR(100) NOT NULL,
	`address` VARCHAR(250)
) ENGINE=InnoDB;


CREATE TABLE `order` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product` VARCHAR(100) NOT NULL,
	`count` INT UNSIGNED NOT NULL,
	`price` DECIMAL(10, 2) UNSIGNED NOT NULL,
    `customer_id` BIGINT UNSIGNED NOT NULL,
    INDEX `idx_order_customer_id` (`customer_id`),
	CONSTRAINT `fk_order_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB;

DELIMITER $$

CREATE TRIGGER `trg_user_adr` 
 AFTER DELETE on `user`
 FOR EACH ROW
BEGIN
	DELETE FROM `principal` WHERE id = old.id;
END$$


CREATE TRIGGER `trg_group_adr` 
 AFTER DELETE on `group`
 FOR EACH ROW
BEGIN
	DELETE FROM `principal` WHERE id = old.id;
END$$

DELIMITER ;

-- ACL TABLES ------------------------------------------------------------------------


CREATE TABLE `acl_object_identity` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`object_type_id` BIGINT UNSIGNED NOT NULL,
	`object_id` BIGINT UNSIGNED NOT NULL,
	`parent_id` BIGINT UNSIGNED,
	`owner_id` BIGINT UNSIGNED,
	`inheriting` BOOLEAN NOT NULL DEFAULT FALSE,
	UNIQUE KEY `uk_acl_object_identity` (`object_type_id`, `object_id`),
	INDEX `idx_acl_object_identity_parent_id` (`parent_id`),
	INDEX `idx_acl_object_identity_owner_id` (`owner_id`),
	CONSTRAINT `fk_acl_object_identity_object_type_id` FOREIGN KEY (`object_type_id`) REFERENCES `object_type` (`id`),
	CONSTRAINT `fk_acl_object_identity_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `acl_object_identity` (`id`) ON DELETE SET NULL,
	CONSTRAINT `fk_acl_object_identity_owner_id` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `acl_entry` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`object_identity_id` BIGINT UNSIGNED NOT NULL,
    `principal_id` BIGINT UNSIGNED NOT NULL,
	`permission_mask` INT UNSIGNED NOT NULL,
	UNIQUE KEY `uk_acl_entry` (`object_identity_id`, `principal_id`),
	INDEX `idx_acl_entry_principal_id` (`principal_id`),
	CONSTRAINT `fk_acl_entry_identity_id` FOREIGN KEY (`object_identity_id`) REFERENCES `acl_object_identity` (`id`),
	CONSTRAINT `fk_acl_entry_principal_id` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`)
) ENGINE=InnoDB;


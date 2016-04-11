CREATE SCHEMA IF NOT EXISTS security_sample 
	DEFAULT CHARACTER SET utf8 
    DEFAULT COLLATE utf8_unicode_ci;

USE security_sample;

DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `contact`;
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
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier',
	`name` VARCHAR(20) NOT NULL COMMENT 'Action name',
	`description` VARCHAR(250) COMMENT 'Action description',
	UNIQUE KEY `uk_action_name`(`name`)
) ENGINE=InnoDB
	COMMENT = 'Actions allowed on business objects';


CREATE TABLE `object_type` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier',
	`name` VARCHAR(100) NOT NULL COMMENT 'Object type name',
	`description` VARCHAR(250) COMMENT 'Object type description',
	UNIQUE KEY `uk_object_type_name`(`name`)
) ENGINE=InnoDB
	COMMENT = 'Types of business object';


CREATE TABLE `privilege` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier',
	`object_type_id` BIGINT UNSIGNED NOT NULL COMMENT 'Object type identifier',
	`action_id` BIGINT UNSIGNED NOT NULL COMMENT 'Action identifier',
    UNIQUE KEY `uk_privilege_obj_type_action` (`object_type_id`, `action_id`),
	CONSTRAINT `fk_privilege_object_type_id` FOREIGN KEY (`object_type_id`) REFERENCES `object_type` (`id`),
	CONSTRAINT `fk_privilege_action` FOREIGN KEY (`action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB
	COMMENT = 'Privileges to perform action on business object';


CREATE TABLE `role` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier',
	`name` VARCHAR(50) NOT NULL COMMENT 'Role name',
	`description` VARCHAR(250) COMMENT 'Role description',
	`parent_id` BIGINT UNSIGNED COMMENT 'Parent role identifier',
    UNIQUE KEY `uk_role_name` (`name`),
	CONSTRAINT `fk_role_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB
	COMMENT = 'Roles of a user in the application';


CREATE TABLE `principal` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier'
) ENGINE=InnoDB
    COMMENT = 'Security principals';


CREATE TABLE `group` (
	`id` BIGINT UNSIGNED NOT NULL PRIMARY KEY COMMENT 'Identifier',
	`name` VARCHAR(50) NOT NULL COMMENT 'Group name',
	`description` VARCHAR(250) COMMENT 'Group description',
    UNIQUE KEY `uk_group_name` (`name`),
    CONSTRAINT `fk_group_principle_id` FOREIGN KEY (`id`) REFERENCES `principal` (`id`)
) ENGINE=InnoDB
    COMMENT = 'Groups of users';


CREATE TABLE `user` (
	`id` BIGINT UNSIGNED NOT NULL PRIMARY KEY COMMENT 'Identifier',
	`user_name` VARCHAR(50) NOT NULL COMMENT 'User name',
	`password` VARCHAR(60) NOT NULL COMMENT 'Password hash',
	`first_name` VARCHAR(50) NOT NULL COMMENT 'First name',
	`last_name` VARCHAR(50) NOT NULL COMMENT 'Surname',
	`email` VARCHAR(100) NOT NULL COMMENT 'E-mail',
    `active` BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Flag of active user',
    UNIQUE KEY `uk_user_user_name` (`user_name`),
    CONSTRAINT `fk_user_principle_id` FOREIGN KEY (`id`) REFERENCES `principal` (`id`)
) ENGINE=InnoDB
    COMMENT = 'Users';


CREATE TABLE `user_role` (
	`user_id` BIGINT UNSIGNED NOT NULL COMMENT 'User identifier',
	`role_id` BIGINT UNSIGNED NOT NULL COMMENT 'Role identifier',
    PRIMARY KEY (`user_id`, `role_id`),
	CONSTRAINT `fk_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `fk_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB
	COMMENT = 'Roles assigned to users';


CREATE TABLE `user_group` (
	`user_id` BIGINT UNSIGNED NOT NULL COMMENT 'User identifier',
	`group_id` BIGINT UNSIGNED NOT NULL COMMENT 'Group identifier',
    PRIMARY KEY (`user_id`, `group_id`),
	CONSTRAINT `fk_user_group_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `fk_user_group_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)
) ENGINE=InnoDB
	COMMENT = 'Users included to groups';


CREATE TABLE `role_privilege` (
	`role_id` BIGINT UNSIGNED NOT NULL COMMENT 'Role identifier',
	`privilege_id` BIGINT UNSIGNED NOT NULL COMMENT 'Privilege identifier',
    PRIMARY KEY (`role_id`, `privilege_id`),
	CONSTRAINT `fk_role_privilege_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
	CONSTRAINT `fk_role_privilege_privilege_id` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
) ENGINE=InnoDB
	COMMENT = 'Privileges assigned to roles';


CREATE TABLE `acl_object_identity` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier',
	`object_type_id` BIGINT UNSIGNED NOT NULL COMMENT 'Object type identifier',
	`object_id` BIGINT UNSIGNED NOT NULL COMMENT 'Object identifier',
	`parent_id` BIGINT UNSIGNED COMMENT 'Parent ACL identity identifier',
	`owner_id` BIGINT UNSIGNED COMMENT 'Owner identifier',
	`inheriting` BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Flag of inheritance from parent',
	UNIQUE KEY `uk_acl_object_identity` (`object_type_id`, `object_id`),
	INDEX `idx_acl_object_identity_parent_id` (`parent_id`),
	INDEX `idx_acl_object_identity_owner_id` (`owner_id`),
	CONSTRAINT `fk_acl_object_identity_object_type_id` FOREIGN KEY (`object_type_id`) REFERENCES `object_type` (`id`),
	CONSTRAINT `fk_acl_object_identity_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `acl_object_identity` (`id`) ON DELETE SET NULL,
	CONSTRAINT `fk_acl_object_identity_owner_id` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB
	COMMENT = 'ACL identities. Represents domain object instances';


CREATE TABLE `acl_entry` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Identifier',
	`object_identity_id` BIGINT UNSIGNED NOT NULL COMMENT 'ACL identity identifier',
    `principal_id` BIGINT UNSIGNED NOT NULL COMMENT 'Principal identifier',
	`permission_mask` INT UNSIGNED NOT NULL COMMENT 'Binary mask for permissions (READ – 1, WRITE – 2, CREATE – 4, DELETE – 8, ADMIN - 16)',
	UNIQUE KEY `uk_acl_entry` (`object_identity_id`, `principal_id`),
	INDEX `idx_acl_entry_principal_id` (`principal_id`),
	CONSTRAINT `fk_acl_entry_identity_id` FOREIGN KEY (`object_identity_id`) REFERENCES `acl_object_identity` (`id`),
	CONSTRAINT `fk_acl_entry_principal_id` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`)
) ENGINE=InnoDB
	COMMENT = 'ACL entries. Permissions that principal has on object';

	
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


-- BUSINESS TABLES ------------------------------------------------------------------------
	
	
CREATE TABLE `contact` (
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
    `contact_id` BIGINT UNSIGNED NOT NULL,
    INDEX `idx_order_contact_id` (`contact_id`),
	CONSTRAINT `fk_order_contact_id` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
) ENGINE=InnoDB;
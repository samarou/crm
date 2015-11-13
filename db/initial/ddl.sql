USE security_sample;

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


CREATE TABLE `group` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(250),
    UNIQUE KEY `uk_group_name` (`name`)
) ENGINE=InnoDB;


CREATE TABLE `user` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`user_name` VARCHAR(50) NOT NULL,
	`password` VARCHAR(60) NOT NULL,
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY `uk_user_user_name` (`user_name`)
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
	CONSTRAINT `fk_order_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB;


-- ACL TABLES ------------------------------------------------------------------------

CREATE TABLE `acl_class` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`class` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `uk_acl_class` (`class`)
) ENGINE=InnoDB;


CREATE TABLE `acl_sid` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`principal` TINYINT(1) NOT NULL,
	`sid` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `unique_acl_sid` (`sid` , `principal`)
)  ENGINE=InnoDB;


CREATE TABLE `acl_object_identity` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`object_id_class` BIGINT(20) UNSIGNED NOT NULL,
	`object_id_identity` BIGINT(20) NOT NULL,
	`parent_object` BIGINT(20) UNSIGNED DEFAULT NULL,
	`owner_sid` BIGINT(20) UNSIGNED DEFAULT NULL,
	`entries_inheriting` TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `uk_acl_object_identity` (`object_id_class`,`object_id_identity`),
	INDEX `fk_acl_object_identity_parent` (`parent_object`),
	INDEX `fk_acl_object_identity_owner` (`owner_sid`),
	CONSTRAINT `fk_acl_object_identity_class` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
	CONSTRAINT `fk_acl_object_identity_owner` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`),
	CONSTRAINT `fk_acl_object_identity_parent` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `acl_entry` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`acl_object_identity` BIGINT(20) UNSIGNED NOT NULL,
	`ace_order` INT(11) NOT NULL,
	`sid` BIGINT(20) UNSIGNED NOT NULL,
	`mask` INT(10) UNSIGNED NOT NULL,
	`granting` TINYINT(1) NOT NULL,
	`audit_success` TINYINT(1) NOT NULL,
	`audit_failure` TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `unique_acl_entry` (`acl_object_identity`,`ace_order`),
	INDEX `fk_acl_entry_acl` (`sid`),
	CONSTRAINT `fk_acl_entry_acl` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`),
	CONSTRAINT `fk_acl_entry_object` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`)
) ENGINE=InnoDB;




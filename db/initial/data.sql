USE security_sample;

-- COMMON DATA -------------------------------------------------------------------

INSERT INTO `action` (`id`, `name`, `description`) VALUES 
(1, 'READ', 'READ ACTION'),
(2, 'WRITE', 'WRITE ACTION'),
(3, 'DELETE', 'DELETE ACTION'),
(4, 'ADMIN', 'ADMIN ACTION');

INSERT INTO `object_type` (`id`, `name`, `description`) VALUES 
(1, 'sample.Customer', ''),
(2, 'sample.Order', '');

INSERT INTO `privilege` (`id`, `object_type_id`, `action_id`) VALUES 
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 2, 1),
(6, 2, 2),
(7, 2, 3),
(8, 2, 4);

INSERT INTO `principal` (`id`) VALUES 
(1),
(2),
(3),
(4), 
(5),
(6);

INSERT INTO `user` (`id`, `user_name`, `password`, `first_name`, `last_name`, `email`, `active`) VALUES 
(1, 'admin', '$2a$10$gXqyc1sgCadb6XnCsTkm/OInAIo6BfR3pw2AHhGiHDT3buRaS1itO', 'brad', 'pitt', 'test2@test.com', true),
(2, 'manager', '$2a$04$aYm0Ym1rneEg6cgB06FxQebV0EhfmUoFYpkJsvg6fHgp392d.EXXG', 'john', 'smith', 'test1@test.com', true),
(3, 'specialist', '$2a$04$tr7WDPbaGwBQ/IMiZDFSmueNaQgTlPBv/08huLxQ3lY8a6VZYMKkG', 'tom', 'sawyer', 'test3@test.com', true);

INSERT INTO `group` (`id`, `name`, `description`) VALUES 
(4, 'admins', 'admins group'),
(5, 'managers', 'managers group'),
(6, 'specialists', 'specialists group');

INSERT INTO `role` (`id`, `name`, `description`, `parent_id`) VALUES 
(1, 'USER', 'USER ROLE', null),
(2, 'ADMIN', 'ADMIN ROLE', 1),
(3, 'MANAGER', 'MANAGER ROLE', 1),
(4, 'SPECIALIST', 'SPECIALIST ROLE', 1);

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 2),
(2, 3),
(3, 4);

INSERT INTO `user_group` (`user_id`, `group_id`) VALUES
(1, 4),
(2, 5),
(3, 6);

INSERT INTO `role_privilege` (`role_id`, `privilege_id`) VALUES 
(1, 1),
(1, 5),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6),
(3, 7),
(3, 8);

INSERT INTO `customer` (`id`, `firstName`, `lastName`, `email`, `address`) VALUES 
(1, 'john', 'smith', '123@gmail.com', 'addr'),
(2, 'bill', 'gates', '456@gmail.com', null),
(3, 'obi-wan', 'cenoby', '456@gmail.com', null),
(4, 'tom', 'brown', '458@gmail.com', null),
(5, 'zakk', 'wylde', '454@gmail.com', null);

INSERT INTO `order` (`id`, `product`, `count`, `price`, `customer_id`) VALUES 
(1, 'book', '2', '12.75', 1),
(2, 'magazine', '1', '5', 1),
(3, 'ball', '1', '99', 2);

-- ACL DATA -------------------------------------------------------------------


INSERT INTO `acl_object_identity` (`id`, `object_type_id`, `object_id`, `parent_id`, `owner_id`, `inheriting`) VALUES 
(1, 1, 1, null, 2, true),
(2, 1, 2, null, 2, true),
(3, 1, 3, null, 2, true),
(4, 1, 4, null, 2, true),
(5, 1, 5, null, 2, true);


INSERT INTO `acl_entry` (`id`, `object_identity_id`, `principal_id`, `permission_mask`) VALUES
(1, 1, 1, 3),
(2, 2, 2, 3);


-- SEQUENCE FIX ---------------------------------------------------------------

ALTER TABLE `action` AUTO_INCREMENT = 10;
ALTER TABLE `object_type` AUTO_INCREMENT = 10;
ALTER TABLE `privilege` AUTO_INCREMENT = 10;
ALTER TABLE `principal` AUTO_INCREMENT = 10;
ALTER TABLE `role` AUTO_INCREMENT = 10;
ALTER TABLE `customer` AUTO_INCREMENT = 10;
ALTER TABLE `order` AUTO_INCREMENT = 10;
ALTER TABLE `acl_object_identity` AUTO_INCREMENT = 10;
ALTER TABLE `acl_entry` AUTO_INCREMENT = 10;


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

INSERT INTO `user` (`id`, `user_name`, `password`, `active`) VALUES 
(1, 'test', '$2a$10$0IY.OHOugvJ8MnUTVczIjuFg710aQf/6KQSVAd9oBRtvC91GFkU16user', true),
(2, 'admin', '$2a$10$gXqyc1sgCadb6XnCsTkm/OInAIo6BfR3pw2AHhGiHDT3buRaS1itO', true);

INSERT INTO `group` (`id`, `name`, `description`) VALUES 
(1, 'viewers', 'viewers group'),
(2, 'managers', 'managers group');

INSERT INTO `role` (`id`, `name`, `description`, `parent_id`) VALUES 
(1, 'ROOT', 'ROOT ROLE', null),
(2, 'USER', 'USER ROLE', 1),
(3, 'ADMIN', 'ADMIN ROLE', 2),
(4, 'GUEST', 'GUEST ROLE', 1);

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

INSERT INTO `user_group` (`user_id`, `group_id`) VALUES
(1, 1),
(2, 2);

INSERT INTO `role_privilege` (`role_id`, `privilege_id`) VALUES 
(1, 1),
(1, 5),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8);

INSERT INTO `customer` (`id`, `firstName`, `lastName`, `email`, `address`) VALUES 
(1, 'john', 'smith', '123@gmail.com', 'addr'),
(2, 'bill', 'gates', '456@gmail.com', null);

INSERT INTO `order` (`id`, `product`, `count`, `price`, `customer_id`) VALUES 
(1, 'book', '2', '12.75', 1),
(2, 'magazine', '1', '5', 1),
(3, 'ball', '1', '99', 2);

-- ACL DATA -------------------------------------------------------------------








-- SEQUENCE FIX ---------------------------------------------------------------

ALTER TABLE `action` AUTO_INCREMENT = 10;
ALTER TABLE `object_type` AUTO_INCREMENT = 10;
ALTER TABLE `privilege` AUTO_INCREMENT = 10;
ALTER TABLE `user` AUTO_INCREMENT = 10;
ALTER TABLE `group` AUTO_INCREMENT = 10;
ALTER TABLE `role` AUTO_INCREMENT = 10;
ALTER TABLE `customer` AUTO_INCREMENT = 10;
ALTER TABLE `role` AUTO_INCREMENT = 10;
ALTER TABLE `customer` AUTO_INCREMENT = 10;
ALTER TABLE `order` AUTO_INCREMENT = 10;
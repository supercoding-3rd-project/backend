CREATE TABLE `users` (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `user_name` VARCHAR(255) NOT NULL ,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted_at` DATETIME NULL
);

CREATE TABLE `roles` (
    `role_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(255) NOT NULL
);

CREATE TABLE `user_roles` (
    `user_role_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL
);

CREATE TABLE `refresh_tokens` (
    `refresh_token_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `refresh_token` VARCHAR(255) NOT NULL
);
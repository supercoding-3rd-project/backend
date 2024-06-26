CREATE TABLE `users` (
                         `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `email` VARCHAR(255) NOT NULL,
                         `password` VARCHAR(255) NOT NULL,
                         `username` VARCHAR(255) NOT NULL,
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

CREATE TABLE `question_board` (
    `ques_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `content_id` BIGINT NOT NULL,
    `user_id` INT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME NULL,
    `deleted_at` DATETIME NULL
);

CREATE TABLE `content` (
    `content_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `markdown_content` LONGTEXT NOT NULL
);

create table ques_like
(
    `like_id` int auto_increment primary key,
    `ques_id` bigint not null,
    `user_id` bigint not null
);
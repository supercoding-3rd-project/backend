CREATE TABLE `users` (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL,
    `role` VARCHAR(20) NOT NULL,
    `social_id` VARCHAR(20) default "X",
    `social_type` VARCHAR(20) default "X",
    `image_url` VARCHAR(255),
    `refresh_token` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
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
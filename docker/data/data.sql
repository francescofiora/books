SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `books` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `books`;

CREATE USER 'japp'@'%' IDENTIFIED BY 'secret';
GRANT SELECT, INSERT, UPDATE, DELETE ON `books`.* TO 'japp'@'%';
FLUSH PRIVILEGES;

create table author (
       id bigint not null auto_increment,
        first_name varchar(255),
        last_name varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;
    

create table publisher (
       id bigint not null auto_increment,
        publisher_name varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

create table title (
       id bigint not null auto_increment,
        copyright integer,
        edition_number bigint,
        image_file varchar(255),
        language varchar(255),
        price bigint,
        name varchar(255),
        publisher_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

create table title_author (
       title_id bigint not null,
        author_id bigint not null,
        primary key (title_id, author_id)
    ) ENGINE=InnoDB;

ALTER TABLE `title` ADD INDEX(`publisher_id`);

alter table title 
       add constraint FK1_title
       foreign key (publisher_id) 
       references publisher (id);

alter table title_author 
       add constraint FK1_title_author
       foreign key (author_id) 
       references author (id);
       
       
alter table title_author 
       add constraint FK2_title_author
       foreign key (title_id) 
       references title (id);       

COMMIT;

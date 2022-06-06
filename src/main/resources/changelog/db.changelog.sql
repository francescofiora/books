--liquibase formatted sql

--changeset Francesco:2022051001
CREATE TABLE author (
       id bigint not null auto_increment,
        first_name varchar(255),
        last_name varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;
    
--changeset Francesco:2022051002
CREATE TABLE publisher (
       id bigint not null auto_increment,
        publisher_name varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

--changeset Francesco:2022051003
CREATE TABLE title (
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
ALTER TABLE `title` ADD INDEX(`publisher_id`);
ALTER TABLE title
       add constraint FK1_title
       foreign key (publisher_id)
       references publisher (id);

--changeset Francesco:2022051004
CREATE TABLE title_author (
       title_id bigint not null,
        author_id bigint not null,
        primary key (title_id, author_id)
    ) ENGINE=InnoDB;
ALTER TABLE title_author
       add constraint FK1_title_author
       foreign key (author_id)
       references author (id);
ALTER TABLE title_author
       add constraint FK2_title_author
       foreign key (title_id)
       references title (id);       

--changeset Francesco:2022052601
CREATE TABLE permission (
       id bigint not null auto_increment,
       name varchar(100) not null,
       description varchar(255) not null,
       primary key (id)
    ) ENGINE=InnoDB;

--changeset Francesco:2022052602
CREATE TABLE role (
       id bigint not null auto_increment,
       name varchar(100) not null,
       description varchar(255) not null,
       primary key (id)
    ) ENGINE=InnoDB;

--changeset Francesco:2022052603
CREATE TABLE role_permission (
       role_id bigint not null,
        permission_id bigint not null,
        primary key (role_id, permission_id)
    ) ENGINE=InnoDB;
ALTER TABLE role_permission
       add constraint FK1_role_permission
       foreign key (permission_id)
       references permission (id);
ALTER TABLE role_permission
       add constraint FK2_role_permission
       foreign key (role_id)
       references role (id);

--changeset Francesco:2022052604
CREATE TABLE `user` (
       id bigint not null auto_increment,
        account_non_expired smallint default 1,
        account_non_locked smallint default 1,
        credentials_non_expired smallint default 1,
        enabled smallint default 1,
        password varchar(100) not null,
        username varchar(50) not null,
        primary key (id)
    ) ENGINE=InnoDB;
ALTER TABLE `user` ADD UNIQUE(`username`);

--changeset Francesco:2022052605
CREATE TABLE user_role (
       user_id bigint not null,
        role_id bigint not null,
        primary key (user_id, role_id)
    ) ENGINE=InnoDB;
ALTER TABLE user_role
       add constraint FK1_user_role
       foreign key (role_id)
       references role (id);
ALTER TABLE user_role
       add constraint FK2_user_role
       foreign key (user_id)
       references user (id);

--changeset Francesco:2022052606
INSERT INTO `permission` (`id`, `name`, `description`) VALUES
(1, 'OP_ROLE_UPDATE', 'Create, update and delete roles and permissions'),
(2, 'OP_ROLE_READ', 'Read roles and permissions'),
(3, 'OP_USER_UPDATE', 'Create, update and delete users'),
(4, 'OP_USER_READ', 'Read users'),
(5, 'OP_BOOK_UPDATE', 'Create, update and delete books'),
(6, 'OP_BOOK_READ', 'Read books');

INSERT INTO `role` (`id`, `name`, `description`) VALUES
(1, 'ROLE_PERMISSION_ADMIN', 'Administration roles and permissions'),
(2, 'ROLE_USER_ADMIN', 'Administration users'),
(3, 'ROLE_BOOK_ADMIN', 'Administration books'),
(4, 'ROLE_BOOK_READ', 'Read books');

INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1),
(1, 2),
(2, 2),
(2, 3),
(2, 4),
(3, 5),
(3, 6),
(4, 6);

INSERT INTO `user` (`id`, `account_non_expired`, `account_non_locked`,
 `credentials_non_expired`, `enabled`, `password`, `username`)
VALUES (1, '1', '1', '1', '1',
 '$2a$10$PMF.CrlbWKk/AdSmtKiOHu/5Fb9sZsEBSohppyoAKoNJeOhkoixzi', 'roleAdmin');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('1', '1');

INSERT INTO `user` (`id`, `account_non_expired`, `account_non_locked`,
 `credentials_non_expired`, `enabled`, `password`, `username`)
VALUES (2, '1', '1', '1', '1',
 '$2a$10$PMF.CrlbWKk/AdSmtKiOHu/5Fb9sZsEBSohppyoAKoNJeOhkoixzi', 'userAdmin');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('2', '2');

INSERT INTO `user` (`id`, `account_non_expired`, `account_non_locked`,
 `credentials_non_expired`, `enabled`, `password`, `username`)
VALUES (3, '1', '1', '1', '1',
 '$2a$10$PMF.CrlbWKk/AdSmtKiOHu/5Fb9sZsEBSohppyoAKoNJeOhkoixzi', 'bookAdmin');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('3', '3');

INSERT INTO `user` (`id`, `account_non_expired`, `account_non_locked`,
 `credentials_non_expired`, `enabled`, `password`, `username`)
VALUES (4, '1', '1', '1', '1',
 '$2a$10$PMF.CrlbWKk/AdSmtKiOHu/5Fb9sZsEBSohppyoAKoNJeOhkoixzi', 'user');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('4', '4');

COMMIT;

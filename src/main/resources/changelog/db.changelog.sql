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

--changeset Francesco:2022051004
CREATE TABLE title_author (
       title_id bigint not null,
        author_id bigint not null,
        primary key (title_id, author_id)
    ) ENGINE=InnoDB;
ALTER TABLE `title` ADD INDEX(`publisher_id`);
ALTER TABLE title 
       add constraint FK1_title
       foreign key (publisher_id) 
       references publisher (id);
ALTER TABLE title_author 
       add constraint FK1_title_author
       foreign key (author_id) 
       references author (id);
ALTER TABLE title_author 
       add constraint FK2_title_author
       foreign key (title_id) 
       references title (id);       

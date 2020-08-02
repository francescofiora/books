create table author (
       id bigint not null auto_increment,
        first_name varchar(255),
        last_name varchar(255),
        primary key (id)
    );
    

create table publisher (
       id bigint not null auto_increment,
        publisher_name varchar(255),
        primary key (id)
    );

create table title (
       id bigint not null auto_increment,
        copyright integer,
        edition_number bigint,
        image_file varchar(255),
        language varchar(255),
        price bigint,
        title varchar(255),
        publisher_id bigint,
        primary key (id)
    );

create table title_author (
       title_id bigint not null,
        author_id bigint not null,
        primary key (title_id, author_id)
    );

ALTER TABLE `title` ADD INDEX(`publisher_id`);

alter table title 
       add constraint FKcy7tb4c5xk6k4axdpvjlaf870 
       foreign key (publisher_id) 
       references publisher (id);

alter table title_author 
       add constraint FKiwj28xlhljidb17bwomt8uvpc 
       foreign key (author_id) 
       references author (id);
       
       
alter table title_author 
       add constraint FKb28pyr5do2ek8cgjqc55sh5v3 
       foreign key (title_id) 
       references title (id);       

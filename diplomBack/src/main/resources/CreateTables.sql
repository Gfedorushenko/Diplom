create schema if not exists filestorage;

create table if not exists filestorage.USERS
(
                           id serial primary key ,
                           login varchar(50) not null ,
                           password varchar(50) not null,
                           auth_token varchar(300)
);

create table if not exists filestorage.FILES
(
    id serial primary key ,
    file_name varchar(50) not null ,
    file_size int,
    file_hash varchar(300) not null,
    file_data bytea,
    upload_date varchar(50) not null,
    user_id int not null
);


insert into filestorage.USERS(login, password)
values ('alexey',  '123');

insert into filestorage.USERS(login, password)
values ('ivan',  '111');
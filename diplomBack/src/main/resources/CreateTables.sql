create schema if not exists ddl;

create table if not exists ddl.USERS
(
                           id serial primary key ,
                           login varchar(50) not null ,
                           password varchar(50) not null,
                           auth_token varchar(300)
);

create table if not exists ddl.FILES
(
    id serial primary key ,
    file_name varchar(50) not null ,
    file_size int,
    file_hash varchar(300) not null,
    content_Type varchar(100) not null,
    upload_date varchar(50) not null,
    user_id int not null
);


insert into ddl.USERS(login, password)
values ('alexey',  '123');

insert into ddl.USERS(login, password)
values ('ivan',  '111');
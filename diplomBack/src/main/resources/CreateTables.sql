create table if not exists USERS
(
                           id serial primary key ,
                           login varchar(50) not null ,
                           password varchar(50) not null,
                           auth_token varchar(300)
);

create table if not exists FILES
(
    id serial primary key ,
    file_name varchar(50) not null ,
    file_size int,
    file_hash varchar(300) not null,
    file_data bytea,
    upload_date varchar(50) not null,
    user_id int not null
);


insert into USERS(login, password)
values ('alexey',  '123');

insert into USERS(login, password)
values ('ivan',  '111');


create table if not exists users
(
    id serial primary key ,
    login varchar(50) not null ,
    password varchar(50) not null,
    auth_token varchar(300)
);

create table if not exists files
(
    id serial primary key ,
    file_name varchar(50) not null ,
    file_size int,
    file_hash varchar(300) not null,
    file_data bytea,
    upload_date varchar(50) not null,
    user_id int not null
);

insert into users(login, password,auth_token)
values ('alexey',  '123','41c10aef5b249a6b983c16c16fe2cf8d');

insert into users(login, password)
values ('ivan',  '111');

insert into files(file_name, file_size,file_hash,file_data,upload_date,user_id)
values ('test.txt',  3,'698d51a19d8a121ce581499d7b701668','111','2023-05-15 +03','1');
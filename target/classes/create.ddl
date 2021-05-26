-- we don't know how to generate root <with-no-name> (class Root) :(
create table if not exists USER
(
    ID INT identity (1, 1)
        primary key,
    NAME VARCHAR(50) not null,
    SURNAME VARCHAR(20) not null,
    PASSPORT VARCHAR(20) UNIQUE
);

create table if not exists ACCOUNT
(
    id int auto_increment primary key ,
    NUM varchar unique ,
    BALANCE DECIMAL(20, 2),
    USER_ID INT NOT NULL
        references USER (ID)
);

create table if not exists CARD
(
    id int auto_increment primary key ,
    ACCOUNT_ID int not null
        references ACCOUNT (id)
);

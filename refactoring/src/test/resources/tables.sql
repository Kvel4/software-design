create table Product
(
    Pid   serial,
    Name  text not null,
    Price int  not null,

    primary key (Pid)
);
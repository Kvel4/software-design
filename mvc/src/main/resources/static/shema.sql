create table ListsTasks
(
    ListId   serial,
    ListName varchar(50),

    primary key (ListId)
);

create table Tasks
(
    TaskId   serial,
    ListId   int,
    TaskName varchar(50),
    IsReady  boolean default false,

    primary key (TaskId),

    foreign key (ListId) references ListsTasks (ListId) on delete cascade
);

create index find_by_list_id_index on Tasks using btree (ListId);
create table if not exists gruppe_db(
    id serial primary key,
    titel text not null,
    start_person varchar(100),
    geschlossen boolean,
    teilnehmer varchar(100)
);

create table if not exists ausgabe_db(
    gruppe_db integer references gruppe_db(id),
    id serial primary key,
    titel varchar(100),
    summe double precision,
    glauebiger varchar(100),
    schuldner varchar(100)
);

create table if not exists teilnehmer(
    id serial primary key,
    gruppe_db integer references gruppe_db(id)
);




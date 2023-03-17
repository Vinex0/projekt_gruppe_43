create table if not exists gruppe(
    id serial primary key,
    titel varchar(150),
    start_person varchar(100) ,
    geschlossen boolean,
    teilnehmer varchar(100)
);

create table if not exists ausgabe(
    id serial primary key ,
    gruppe integer references gruppe(id),
    titel varchar(100),
    summe double precision,
    glauebiger varchar(100),
    schuldner varchar(100)
);




create table if not exists gruppe(
    id serial primary key ,
    titel varchar(150)  ,
    start_person varchar(100)  ,
    geschlossen boolean
);

create table if not exists teilnehmer(
    id serial primary key ,
    foreign key (id) references gruppe(id),
    person varchar(100)
);

create table if not exists gruppen_teilnehmer(
    gruppe integer references gruppe(id),
    teilnehmer integer references teilnehmer(id)
);

create table if not exists ausgabe(
    id serial primary key ,
    gruppe integer references gruppe(id),
    titel varchar(100),
    summe double precision
);

create table if not exists ausgaben_teilnehmer(
    ausgabe integer references ausgabe(id),
    teilnehmer integer references teilnehmer(id)
);

create table if not exists schulden(
    gruppe integer references gruppe(id),
    glaeubiger integer references teilnehmer(id),
    schuldner integer references teilnehmer(id),
    summe double precision
);




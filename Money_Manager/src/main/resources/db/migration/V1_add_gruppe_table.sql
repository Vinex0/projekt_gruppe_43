create table if not exists gruppe(
    id serial primary key ,
    titel varchar(150)  ,
    start_person varchar(100)  ,
    geschlossen boolean
);
create  table gruppen_teilnehmer(
    foreign key (gruppe) references gruppe(id),
    foreign key (teilnehmer) references teilnehmer(id)
);

create table teilnehmer(
    id serial primary key ,
    foreign key (gruppe) references gruppe(id),
    person varchar(100)
);

create table ausgaben_teilnehmer(
    foreign key (ausgabe) references ausgabe(id),
    foreign key (teilnehmer) references teilnehmer(id)
);

create table ausgabe(
    id serial primary key ,
    foreign key (gruppe) references gruppe(id),
    titel varchar(100),
    foreign key (glaeubiger) references teilnehmer(id),
    summe double
);

create table schulden(
    foreign key (gruppe) references gruppe(id),
    foreign key (glaeubiger) references teilnehmer(id),
    foreign key (schuldner) references teilnehmer(id),
    summe double
);




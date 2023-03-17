package com.gruppe43.moneymanager.database;

import java.awt.print.Book;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

//TODO
@Table("gruppe")
public record GruppeDb(@Id int id, String titel, String startPerson, boolean geschlossen, List<String> teilnehmer, List<AusgabeDb> ausgabedbs) {

}

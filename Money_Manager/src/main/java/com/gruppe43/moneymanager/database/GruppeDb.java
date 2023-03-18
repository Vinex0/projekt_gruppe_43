package com.gruppe43.moneymanager.database;


import java.util.List;
import org.springframework.data.annotation.Id;


public record GruppeDb(@Id Integer id, String titel, String startPerson, boolean geschlossen,
                       List<String> teilnehmer, List<AusgabeDb> ausgaben) {

}

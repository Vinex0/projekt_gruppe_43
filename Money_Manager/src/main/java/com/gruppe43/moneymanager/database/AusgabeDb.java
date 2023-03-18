package com.gruppe43.moneymanager.database;


import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;


record AusgabeDb(AggregateReference<GruppeDb, Integer> gruppeDbKey, String titel, Double summe,
                 String glauebiger, List<String> schuldner) {


}

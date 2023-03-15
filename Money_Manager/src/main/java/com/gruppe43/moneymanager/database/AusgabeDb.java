package com.gruppe43.moneymanager.database;

import java.util.List;
import org.javamoney.moneta.Money;

public record AusgabeDb(String titel, Money summe, String glaeubiger, List<String> teilnehmer) {

}

package com.gruppe43.moneymanager.domain;

import java.util.List;
import org.javamoney.moneta.Money;

public record Ausgabe(String title, Person glaeubiger, List<Person> teilnehmer, Money summe) {

}

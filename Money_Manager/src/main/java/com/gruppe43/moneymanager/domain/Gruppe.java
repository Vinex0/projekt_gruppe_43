package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.javamoney.moneta.Money;

public record Gruppe(String title,
                     List<Person> teilnehmer,
                     ArrayList<Ausgabe> ausgaben,
                     Map<Person, Map<Person, Money>> schulden) {

}

package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javamoney.moneta.Money;

@Getter
@AllArgsConstructor
public class Ausgabe implements Comparable<Ausgabe> {

  private final String glaeubiger;
  private final List<String> schuldnerListe;
  private final String titel;
  private final Money summe;

  public Ausgabe(String glaeubiger, String titel, Money summe) {
    this(glaeubiger, new ArrayList<>(), titel, summe);
  }

  public void addSchuldner(String schuldnerNehmer) {
    schuldnerListe.add(schuldnerNehmer);
  }

  @Override
  public int compareTo(Ausgabe other) {
    return titel.compareTo(other.titel);
  }

}

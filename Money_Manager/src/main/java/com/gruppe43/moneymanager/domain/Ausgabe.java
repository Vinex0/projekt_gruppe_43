package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.javamoney.moneta.Money;

@Getter
public class Ausgabe implements Comparable<Ausgabe> {

  private final Person glaeubiger;
  private final List<Person> schuldnerListe;
  private final String titel;
  private final Money summe;

  public Ausgabe(Person glaeubiger, String titel, Money summe) {
    this(glaeubiger, new ArrayList<>(), titel, summe);
  }

  public Ausgabe(Person glaeubiger, List<Person> schuldnerListe, String titel, Money summe) {
    this.glaeubiger = glaeubiger;
    this.schuldnerListe = schuldnerListe;
    this.titel = titel;
    this.summe = summe;
  }

  public void addSchuldner(Person schuldnerNehmer) {
    schuldnerListe.add(schuldnerNehmer);
  }

  @Override
  public int compareTo(Ausgabe other) {
    return titel.compareTo(other.titel);
  }

}

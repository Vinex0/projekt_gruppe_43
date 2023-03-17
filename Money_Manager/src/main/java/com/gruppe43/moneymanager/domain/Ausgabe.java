package com.gruppe43.moneymanager.domain;


import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.javamoney.moneta.Money;

@Value
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Ausgabe implements Comparable<Ausgabe> {

  String glaeubiger;
  List<String> schuldnerListe;
  String titel;
  Money summe;

  static Ausgabe erstellen(String glaeubiger, String titel, Money summe) {
    return new Ausgabe(glaeubiger, new ArrayList<>(), titel, summe);
  }

  void addSchuldner(String schuldnerNehmer) {
    schuldnerListe.add(schuldnerNehmer);
  }

  @Override
  public int compareTo(Ausgabe other) {
    return titel.compareTo(other.titel);
  }

}

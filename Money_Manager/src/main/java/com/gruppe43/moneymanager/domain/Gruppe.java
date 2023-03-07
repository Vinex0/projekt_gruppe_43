package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.javamoney.moneta.Money;

@EqualsAndHashCode(of = "id")
@Getter
public class Gruppe {

  private final Integer id;
  private final String titel;
  private final Person startPerson;
  private final List<Person> teilnehmer;
  private final List<Ausgabe> ausgaben;
  private final Map<Person, Map<Person, Money>> schulden;

  public Gruppe(String titel, Person startPerson) {
    this(null, titel, startPerson, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
    if (!teilnehmer.contains(startPerson)) {
      teilnehmer.add(startPerson);
      schulden.put(startPerson, new HashMap<>());
    }
  }

  public Gruppe(Integer id, String titel, Person startPerson, List<Person> teilnehmer,
      List<Ausgabe> ausgaben, Map<Person, Map<Person, Money>> schulden) {
    this.id = id;
    this.titel = titel;
    this.startPerson = startPerson;
    this.teilnehmer = teilnehmer;
    this.ausgaben = ausgaben;
    this.schulden = schulden;
  }

  public void addTeilnehmer(Person neuerNutzer) {
    if (ausgaben.isEmpty() && neuerNutzer.compareTo(startPerson) != 0) {
      teilnehmer.add(neuerNutzer);
      schulden.put(neuerNutzer, new HashMap<>());

      for (Person p : teilnehmer) {
        if (!p.equals(neuerNutzer)) {
          schulden.get(neuerNutzer).put(p, Money.of(0, "EUR"));
          schulden.get(p).put(neuerNutzer, Money.of(0, "EUR"));
        }
      }
    }
  }

  public void createAusgabe(Person glaeubiger, List<Person> schuldner, Money summe, String title) {
    Ausgabe ausgabe = new Ausgabe(glaeubiger, title, summe);

    for (Person p : schuldner) {
      ausgabe.addSchuldner(p);
    }

    Money individualAmount = CalculationHelpers.paymentShare(summe,
        ausgabe.getSchuldnerListe().size());

    ausgaben.add(ausgabe);
    for (Person p : ausgabe.getSchuldnerListe()) {
      if (!p.equals(glaeubiger)) {
        Money tmp = schulden.get(p).get(glaeubiger);
        schulden.get(p).put(glaeubiger, tmp.add(individualAmount));
      }
    }
    List<Person> part = new ArrayList<>(ausgabe.getSchuldnerListe());
    adjustSchulden(part);
  }

  private void adjustSchulden(List<Person> involved) {
    for (Person p : involved) {
      for (Person b : involved) {
        if (!b.equals(p)) {
          var amountP = schulden.get(p).get(b);
          var amountB = schulden.get(b).get(p);
          var diff = CalculationHelpers.difference(amountP, amountB);
          var reverseDiff = CalculationHelpers.difference(amountB, amountP);
          if (diff.isGreaterThan(Money.of(0, "EUR"))) {
            schulden.get(p).put(b, diff);
            schulden.get(b).put(p, Money.of(0, "EUR"));
          } else {
            schulden.get(p).put(b, Money.of(0, "EUR"));
            schulden.get(b).put(p, reverseDiff);
          }
        }
      }
    }
  }

  public Map<Person, Money> getGlaeubigerFrom(Person schuldner) {
    if (schulden.containsKey(schuldner)) {
      return schulden.get(schuldner).entrySet().stream()
          .filter((v) -> v.getValue().isGreaterThan(Money.of(0, "EUR")))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
    return null;
  }

  // Helper Ausgleich von A an B
  public Money getSchuldenFromTo(Person schuldner, Person glauebiger) {
    return schulden.get(schuldner).get(glauebiger);
  }


}


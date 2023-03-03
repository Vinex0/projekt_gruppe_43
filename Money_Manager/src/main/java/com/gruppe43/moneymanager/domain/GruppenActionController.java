package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;

public class GruppenActionController {

  private final Gruppe gruppe;

  public GruppenActionController(String title, Person ersteller) {
    this.gruppe = new Gruppe(title, new ArrayList<Person>(), new ArrayList<Ausgabe>(),
        new HashMap<Person, Map<Person, Money>>());
    addNutzer(ersteller);
  }

  void addNutzer(Person neuerNutzer) {
    neuerNutzer.addGruppe(gruppe);
    gruppe.teilnehmer().add(neuerNutzer);
    gruppe.schulden().put(neuerNutzer, new HashMap<Person, Money>());
    for (Person p : gruppe.teilnehmer()) {
      if (!p.equals(neuerNutzer)) {
        gruppe.schulden().get(neuerNutzer).put(p, Money.of(0, "EUR"));
        gruppe.schulden().get(p).put(neuerNutzer, Money.of(0, "EUR"));
      }
    }

  }

  public List<Person> getAnderePersonen(Person glaeubiger) {
    return new ArrayList<>(gruppe.schulden().get(glaeubiger).keySet());
  }

  public Map<Person, Money> getGlaeubiger(Person schuldner) {
    if (gruppe.schulden().containsKey(schuldner)) {
      return gruppe.schulden().get(schuldner).entrySet().stream()
          .filter((v) -> v.getValue().isGreaterThan(Money.of(0, "EUR")))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    }
    return null;
  }

  public void createAusgabe(Person glaeubiger, List<Person> schuldner, Money summe, String title) {
    Money individualAmount = CalculationHelpers.paymentShare(summe, schuldner.size());
    Ausgabe e = new Ausgabe(title, glaeubiger, schuldner, summe);
    gruppe.ausgaben().add(e);
    for (Person p : schuldner) {
      if (!p.equals(glaeubiger)) {
        Money tmp = gruppe.schulden().get(p).get(glaeubiger);
        gruppe.schulden().get(p).put(glaeubiger, tmp.add(individualAmount));
      }
    }
    List<Person> part = new ArrayList<>(schuldner);
    adjustSchulden(part);
  }

  private void adjustSchulden(List<Person> involved) {
    for (Person p : involved) {
      for (Person b : involved) {
        if (!b.equals(p)) {
          var amountP = gruppe.schulden().get(p).get(b);
          var amountB = gruppe.schulden().get(b).get(p);
          var diff = CalculationHelpers.difference(amountP, amountB);
          var reverseDiff = CalculationHelpers.difference(amountB, amountP);
          if (diff.isGreaterThan(Money.of(0, "EUR"))) {
            gruppe.schulden().get(p).put(b, diff);
            gruppe.schulden().get(b).put(p, Money.of(0, "EUR"));
          } else {
            gruppe.schulden().get(p).put(b, Money.of(0, "EUR"));
            gruppe.schulden().get(b).put(p, reverseDiff);
          }
        }
      }
    }
  }

  public List<Money> getSchulden(Person person) {
    return new ArrayList<>(gruppe.schulden().get(person).values());
  }

  //Fuer test Szenario 4..
  public void closeGroup() {
    adjustSchulden(gruppe.teilnehmer());
  }

  public Gruppe getGroup() {
    return gruppe;
  }

  public List<Ausgabe> getExpenses() {
    return gruppe.ausgaben();
  }

}


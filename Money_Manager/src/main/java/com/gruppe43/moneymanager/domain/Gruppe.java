package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.javamoney.moneta.Money;

@EqualsAndHashCode(of = "id")
@Getter
@AllArgsConstructor
@ToString
public class Gruppe {

  private final String id;
  private final String titel;
  private final String startPerson;
  private final List<String> teilnehmer;
  private final List<Ausgabe> ausgaben;
  private final Map<String, Map<String, Money>> schulden;

  private boolean closed;

  public Gruppe(String titel, String startPerson) {
    this(null, titel, startPerson, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), false);
    if (!teilnehmer.contains(startPerson)) {
      teilnehmer.add(startPerson);
      schulden.put(startPerson, new HashMap<>());
    }
  }

  public Gruppe(String titel, List<String> personen) {
    this(null, titel, personen.get(0), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), false);
    for (String p : personen) {
      if(!teilnehmer.contains(p)) {
        addTeilnehmer(p);
      }
    }
  }

  public Gruppe(String titel, String startPerson, String id) {
    this(id, titel, startPerson, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), false);
    if (!teilnehmer.contains(startPerson)) {
      teilnehmer.add(startPerson);
      schulden.put(startPerson, new HashMap<>());
    }

  }

  public void addTeilnehmer(String neuerNutzer) {
    if(closed) throw new RuntimeException("Group already closed");
    if (ausgaben.isEmpty() && neuerNutzer.compareTo(startPerson) != 0) {
      teilnehmer.add(neuerNutzer);
      schulden.put(neuerNutzer, new HashMap<>());

      for (String p : teilnehmer) {
        if (!p.equals(neuerNutzer)) {
          schulden.get(neuerNutzer).put(p, Money.of(0, "EUR"));
          schulden.get(p).put(neuerNutzer, Money.of(0, "EUR"));
        }
      }
    }
  }

  public void createAusgabe(String glaeubiger, List<String> schuldner, Money summe, String title) {
    if(closed) throw new RuntimeException("Group already closed");
    Ausgabe ausgabe = new Ausgabe(glaeubiger, title, summe);

    for (String p : schuldner) {
      ausgabe.addSchuldner(p);
    }

    Money individualAmount = CalculationHelpers.paymentShare(summe,
        ausgabe.getSchuldnerListe().size());

    ausgaben.add(ausgabe);
    for (String p : ausgabe.getSchuldnerListe()) {
      if (!p.equals(glaeubiger)) {
        Money tmp = schulden.get(p).get(glaeubiger);
        schulden.get(p).put(glaeubiger, tmp.add(individualAmount));
      }
    }
    List<String> part = new ArrayList<>(ausgabe.getSchuldnerListe());
    adjustSchulden(part);
  }

  private void adjustSchulden(List<String> involved) {
    for (String a : involved) {
      for (String b : involved) {
        if (!b.equals(a)) {
          Money amountA = getSchuldenFromTo(a, b);
          Money amountB = getSchuldenFromTo(b, a);
          Money diff = CalculationHelpers.difference(amountA, amountB);
          Money reverseDiff = CalculationHelpers.difference(amountB, amountA);
          if (diff.isGreaterThan(Money.of(0, "EUR"))) {
            schulden.get(a).put(b, diff);
            schulden.get(b).put(a, Money.of(0, "EUR"));
          } else {
            schulden.get(a).put(b, Money.of(0, "EUR"));
            schulden.get(b).put(a, reverseDiff);
          }
        }
      }
    }
  }

  public Map<String, Money> getGlaeubigerFrom(String schuldner) {
    if (schulden.containsKey(schuldner)) {
      return schulden.get(schuldner).entrySet().stream()
          .filter((v) -> v.getValue().isGreaterThan(Money.of(0, "EUR")))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
    return null;
  }

  // Helper Ausgleich von A an B
  public Money getSchuldenFromTo(String schuldner, String glauebiger) {
    return schulden.get(schuldner).get(glauebiger);
  }

  public Money getTotalSchuldenFrom(String schuldner) {
    Money betrag = Money.of(0, "EUR");
    for (Money m : getGlaeubigerFrom(schuldner).values()) {
      betrag.add(m);
    }
    return betrag;
  }

  public void close() {
    closed = true;
  }

}


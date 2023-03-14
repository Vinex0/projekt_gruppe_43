package com.gruppe43.moneymanager.domain;

import com.gruppe43.moneymanager.domain.dto.AusgabeDto;
import com.gruppe43.moneymanager.stereotypes.AggregateRoot;
import com.gruppe43.moneymanager.stereotypes.ClassOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
@AggregateRoot
public class Gruppe {

  private final String id;
  private final String titel;
  private final String startPerson;
  private final List<String> teilnehmer;
  private final List<Ausgabe> ausgaben;
  private final Map<String, Map<String, Money>> schulden;


  private boolean closed;

  public Gruppe(String titel, String startPerson) {
    this(null,
        Objects.requireNonNull(titel),
        Objects.requireNonNull(startPerson),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashMap<>(),
        false);

    if (!teilnehmer.contains(startPerson)) {
      teilnehmer.add(startPerson);
      schulden.put(startPerson, new HashMap<>());
    }
  }

  public Gruppe(String titel, List<String> personen) {
    this(null,
        Objects.requireNonNull(titel),
        personen.get(0),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashMap<>(),
        false);

    for (String p : personen) {
      if (!teilnehmer.contains(p)) {
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
    if (closed) {
      throw new RuntimeException("Group already closed");
    }
    if (ausgaben.isEmpty() && neuerNutzer.compareTo(startPerson) != 0) {
      initializeSchuldenMap(neuerNutzer);
    }
  }

  @ClassOnly
  private void initializeSchuldenMap(String neuerNutzer) {
    teilnehmer.add(neuerNutzer);
    schulden.put(neuerNutzer, new HashMap<>());

    for (String p : teilnehmer) {
      if (!p.equals(neuerNutzer)) {
        schulden.get(neuerNutzer).put(p, Money.of(0, "EUR"));
        schulden.get(p).put(neuerNutzer, Money.of(0, "EUR"));
      }
    }
  }

  public void createAusgabe(String glaeubiger, List<String> schuldner, Money summe, String title) {
    if (closed) {
      throw new RuntimeException("Group already closed");
    }
    Ausgabe ausgabe = Ausgabe.erstellen(glaeubiger, title, summe);

    for (String p : schuldner) {
      ausgabe.addSchuldner(p);
    }

    if(schuldner.size() >= 1) {
      Money individualAmount = summe.divide(schuldner.size());
      ausgaben.add(ausgabe);
      createIndividualSchuld(glaeubiger, ausgabe, individualAmount);
    } else {
      System.err.println("Teilnehmer darf nicht 0 sein");
    }

    //adjustSchulden(ausgabe.getSchuldnerListe());
  }

  private void createIndividualSchuld(String glaeubiger, Ausgabe ausgabe, Money individualAmount) {
    for (String person : ausgabe.getSchuldnerListe()) {
      if (!person.equals(glaeubiger)) {
        schulden.get(person)
            .put(glaeubiger, getSchuldenFromTo(person, glaeubiger).add(individualAmount));
      }
    }
  }

  private void adjustSchulden(List<String> involved) {

    for (String a : involved) {
      for (String b : involved) {
        if (!b.equals(a)) {
          Money amountA = getSchuldenFromTo(a, b);
          Money amountB = getSchuldenFromTo(b, a);
          Money diffFromAtoB = amountA.subtract(amountB);
          Money diffFromBtoA = amountB.subtract(amountA);
          if (diffFromAtoB.isGreaterThan(Money.of(0, "EUR"))) {
            schulden.get(a).put(b, diffFromAtoB);
            schulden.get(b).put(a, Money.of(0, "EUR"));
          } else {
            schulden.get(a).put(b, Money.of(0, "EUR"));
            schulden.get(b).put(a, diffFromBtoA);
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
    double betrag = 0d;
    for (Money m : getGlaeubigerFrom(schuldner).values()) {
      betrag += m.getNumber().doubleValue();
    }
    return Money.of(betrag, "EUR");
  }

  @ClassOnly
  private Money getTotalPaymentFrom(String glaeubiger) {
    double betrag = 0d;
    for (Ausgabe ausgabe : ausgaben) {
      if (glaeubiger.equals(ausgabe.getGlaeubiger())) {

        if (ausgabe.getSchuldnerListe().contains(glaeubiger)) {
          double betweenSum =
              ausgabe.getSumme().getNumber().doubleValue() / ausgabe.getSchuldnerListe().size();
          betrag += (ausgabe.getSumme().getNumber().doubleValue() - betweenSum);
        } else {
          betrag += ausgabe.getSumme().getNumber().doubleValue();
        }

      }
    }
    return Money.of(betrag, "EUR");
  }

  public void close() {
    adjustSchuldenV2();
    closed = true;
  }

  public List<AusgabeDto> getAusgabeDto(){

    return ausgaben.stream()
       .map(a-> new AusgabeDto(a.getGlaeubiger(),
           a.getSchuldnerListe(),
           a.getTitel(),
           a.getSumme()))
       .collect(Collectors.toList());
  }


  @ClassOnly
  private void adjustSchuldenV2() {
    Map<String, Money> debtMap = new HashMap<>();
    Map<String, Money> creditMap = new HashMap<>();

    Map<String, Money> mustPay = new HashMap<>();
    Map<String, Money> mustGet = new HashMap<>();

    Map<String, Money> descPayMap = new LinkedHashMap<>();
    Map<String, Money> ascCreditMap = new LinkedHashMap<>();

    //Adjust initial amount
    for (String person : teilnehmer) {
      debtMap.put(person, getTotalSchuldenFrom(person));
      creditMap.put(person, getTotalPaymentFrom(person));
    }

    //Adjust mustPay and mustGet
    for (String person : teilnehmer) {
      if (debtMap.containsKey(person) && creditMap.containsKey(person)) {
        if (creditMap.get(person).subtract(debtMap.get(person)).isLessThan(Money.of(0, "EUR"))) {
          mustGet.put(person, Money.of(0, "EUR"));
          mustPay.put(person, debtMap.get(person).subtract(creditMap.get(person)));
        } else if (creditMap.get(person).subtract(debtMap.get(person))
            .isEqualTo(Money.of(0, "EUR"))) {
          mustGet.put(person, Money.of(0, "EUR"));
          mustPay.put(person, Money.of(0, "EUR"));
        } else {
          mustGet.put(person, creditMap.get(person).subtract(debtMap.get(person)));
          mustPay.put(person, Money.of(0, "EUR"));
        }

      }
    }

    //Get ascending order of payment
    for (String person : teilnehmer) {
      for (String personB : teilnehmer) {
        if (!personB.equals(person)) {
          Map.Entry<String, Money> maxSchuldner = null;
          if (!descPayMap.containsKey(person)) {
            for (Map.Entry<String, Money> entry : mustPay.entrySet()) {
              if (maxSchuldner == null
                  || entry.getValue().compareTo(maxSchuldner.getValue()) >= 0) {
                maxSchuldner = entry;
              }
            }
            assert maxSchuldner != null;
            descPayMap.put(maxSchuldner.getKey(), maxSchuldner.getValue());
            mustPay.remove(maxSchuldner.getKey());
          } else if (!descPayMap.containsKey(personB) && mustPay.get(personB).isZero()) {
            descPayMap.put(personB, Money.of(0, "EUR"));
            mustPay.remove(personB);
          }
        }
      }
    }

    //Get descending order of credit
    for (String person : teilnehmer) {
      for (String personB : teilnehmer) {
        if (!personB.equals(person)) {
          Map.Entry<String, Money> minBekommen = null;
          if (!ascCreditMap.containsKey(personB)) {
            for (Map.Entry<String, Money> entry : mustGet.entrySet()) {
              if (minBekommen == null || entry.getValue().compareTo(minBekommen.getValue()) < 0) {
                minBekommen = entry;
              }
            }
            assert minBekommen != null;
            ascCreditMap.put(minBekommen.getKey(), minBekommen.getValue());
            mustGet.remove(minBekommen.getKey());
          } else if (!ascCreditMap.containsKey(person) && mustGet.size() == 1 && mustGet.get(person)
              .isPositiveOrZero()) {
            ascCreditMap.put(person, mustGet.get(person));
            mustGet.remove(person);
          }
        }
      }
    }

    Map<String, Money> copyOfDescPayMap = new LinkedHashMap<>(descPayMap);
    Map<String, Money> copyOfAscCreditMap = new LinkedHashMap<>(ascCreditMap);

    //Do not delete counter, cycle helper
    //Final adjustment
    int counter = 0;
    for (String zahltGeld : copyOfDescPayMap.keySet()) {
      for (String bekommtGeld : copyOfAscCreditMap.keySet()) {

        //Ring
        if (counter < teilnehmer.size()) {
          if (descPayMap.get(bekommtGeld).isEqualTo(ascCreditMap.get(bekommtGeld))
              && descPayMap.get(bekommtGeld) != null && ascCreditMap.get(bekommtGeld) != null) {
            for (String personC : teilnehmer) {
              if (!personC.equals(bekommtGeld)) {
                schulden.get(bekommtGeld).put(personC, Money.of(0, "EUR"));
                schulden.get(personC).put(bekommtGeld, Money.of(0, "EUR"));
              }
            }
          }
        }

        double counterHelper = teilnehmer.size();

        if (counter < Math.pow(counterHelper, 2)) {

          if (ascCreditMap.get(zahltGeld) != null && ascCreditMap.get(zahltGeld)
              .isPositiveOrZero()) { //gets money
            if (!bekommtGeld.equals(zahltGeld)) { // a != b

              for (String s : copyOfDescPayMap.keySet()) {
                if (!s.equals(zahltGeld)) {
                  if (copyOfAscCreditMap.get(zahltGeld).isZero()) {
                    schulden.get(bekommtGeld).put(zahltGeld, Money.of(0, "EUR"));
                  }
                }
              }

              //precision error validation 73.37 !equal 73.37
              if ((ascCreditMap.get(zahltGeld).add(Money.of(0.001, "EUR"))).isGreaterThanOrEqualTo(
                  descPayMap.get(bekommtGeld))) { //if a has higher credit, then b must pay

                Money diff = descPayMap.get(zahltGeld).subtract(ascCreditMap.get(bekommtGeld));
                Money diffInverse = ascCreditMap.get(bekommtGeld)
                    .subtract(descPayMap.get(zahltGeld));

                if (diff.isGreaterThanOrEqualTo(ascCreditMap.get(bekommtGeld)) && !ascCreditMap.get(
                    bekommtGeld).isEqualTo(Money.of(0, "EUR"))) {

                  for (String p : teilnehmer) {
                    if (!p.equals(zahltGeld)) {
                      if (p.equals(bekommtGeld)) {
                        schulden.get(zahltGeld).put(bekommtGeld, ascCreditMap.get(bekommtGeld));
                        schulden.get(bekommtGeld).put(zahltGeld, Money.of(0, "EUR"));
                      }
                    }
                  }

                  ascCreditMap.put(bekommtGeld, Money.of(0, "EUR"));
                  descPayMap.put(zahltGeld, diff);

                } else if (diff.toString().equals(diffInverse.toString())) {  //diff.isZero
                  for (String p : teilnehmer) {
                    if (!p.equals(zahltGeld)) {
                      if (p.equals(bekommtGeld)) {
                        if (!descPayMap.get(zahltGeld).isZero()) {
                          schulden.get(zahltGeld).put(bekommtGeld, descPayMap.get(zahltGeld));
                          schulden.get(bekommtGeld).put(zahltGeld, Money.of(0, "EUR"));
                        }
                      } else if (copyOfDescPayMap.get(p) != null && !copyOfDescPayMap.get(p)
                          .isZero() && copyOfAscCreditMap.get(p).isZero()
                          && copyOfAscCreditMap.get(p) != null) {
                        schulden.get(zahltGeld).put(p, Money.of(0, "EUR"));
                      } else if (copyOfDescPayMap.get(zahltGeld) != null && copyOfDescPayMap.get(
                          zahltGeld).isZero() && ascCreditMap.get(p).isZero()) {
                        schulden.get(zahltGeld).put(p, Money.of(0, "EUR"));
                      }
                    }
                  }
                  ascCreditMap.put(bekommtGeld, Money.of(0, "EUR"));
                  descPayMap.put(zahltGeld, Money.of(0, "EUR"));

                } else if (diffInverse.isPositive() && diff.isNegative()) {

                  for (String person : teilnehmer) {
                    if (!person.equals(zahltGeld)) {
                      if (person.equals(bekommtGeld)) {
                        if (!descPayMap.get(zahltGeld).isZero()) {
                          schulden.get(zahltGeld).put(bekommtGeld, descPayMap.get(zahltGeld));
                          schulden.get(bekommtGeld).put(zahltGeld, Money.of(0, "EUR"));
                          for (String s : teilnehmer) {
                            if (!s.equals(zahltGeld)) {
                              if (!s.equals(bekommtGeld)) {
                                if (!copyOfAscCreditMap.get(person).isZero() && descPayMap.get(s)
                                    .isZero()) {
                                  schulden.get(bekommtGeld).put(s, Money.of(0, "EUR"));
                                }
                              }
                            }
                          }
                        } else {
                          schulden.get(zahltGeld).put(bekommtGeld, Money.of(0, "EUR"));
                        }
                      }
                    }
                  }
                  ascCreditMap.put(bekommtGeld, diffInverse);
                  descPayMap.put(zahltGeld, Money.of(0, "EUR"));
                } else if (diffInverse.isNegative() && diff.isPositive()) {
                  schulden.get(zahltGeld).put(bekommtGeld, Money.of(0, "EUR"));
                }
              }
            }
          }
        }

        counter += 1;
      }
    }

  }

}
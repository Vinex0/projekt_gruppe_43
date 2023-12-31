package com.gruppe43.moneymanager.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

class AusgabeTest {

  @Test
  void test1() {
    Ausgabe ausgabe = Ausgabe.erstellen("A", "ausgabe", Money.of(100, "EUR"));

    assertThat(ausgabe).isNotNull();
  }

  @Test
  void test2() {

    Ausgabe ausgabe = Ausgabe.erstellen("personA", "ausgabe", Money.of(100, "EUR"));
    ausgabe.addSchuldner("personA");
    ausgabe.addSchuldner("personB");

    assertAll(
        () -> assertThat(ausgabe.getGlaeubiger()).isEqualTo("personA"),
        () -> assertThat(ausgabe.getTitel()).isEqualTo("ausgabe"),
        () -> assertThat(ausgabe.getSumme()).isEqualTo(Money.of(100, "EUR")),
        () -> assertThat(ausgabe.getSchuldnerListe().size()).isEqualTo(2)
    );
  }

}
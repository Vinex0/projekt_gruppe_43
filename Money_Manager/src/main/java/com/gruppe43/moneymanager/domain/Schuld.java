package com.gruppe43.moneymanager.domain;

import com.gruppe43.moneymanager.stereotypes.Wertobjekt;
import java.util.Map;

import org.javamoney.moneta.Money;


@Wertobjekt
record Schuld(Map<String, Money> moneyMap) {

  public static Schuld of(Map<String, Money> moneyMap) {
    return new Schuld(moneyMap);
  }

}

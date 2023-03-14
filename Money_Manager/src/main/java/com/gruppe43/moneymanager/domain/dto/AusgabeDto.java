package com.gruppe43.moneymanager.domain.dto;


import java.util.List;

import org.javamoney.moneta.Money;

public class AusgabeDto {
  private String glaeubiger;
  private List<String> schuldnerListe;
  private String titel;
  private Money summe;

  public AusgabeDto(String glaeubiger, List<String> schuldnerListe, String titel, Money summe) {
    this.glaeubiger = glaeubiger;
    this.schuldnerListe = schuldnerListe;
    this.titel = titel;
    this.summe = summe;
  }

  public String getGlaeubiger() {
    return glaeubiger;
  }

  public List<String> getSchuldnerListe() {
    return schuldnerListe;
  }

  public String getTitel() {
    return titel;
  }

  public Money getSumme() {
    return summe;
  }
}


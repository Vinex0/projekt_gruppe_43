package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Ausgabe;
import com.gruppe43.moneymanager.domain.Gruppe;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.javamoney.moneta.Money;

public class SerializerService {

  public static String gruppeToJson(Gruppe gruppe) {
    return "{\"gruppe\" : " + "\"" + gruppe.getId() + "\"" + ", \"name\" : " + "\""
        + gruppe.getTitel() + "\"" + ", \"personen\" : " + personListToJson(gruppe.getTeilnehmer())
        + ", \"geschlossen\" : " + gruppe.isClosed() + ", \"ausgaben\" : " + ausgabenToJson(
        gruppe.getAusgaben()) + "}";
  }

  private static String personListToJson(List<String> personen) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (String p : personen) {
      builder.append("\"");
      builder.append(p);
      builder.append("\", ");
    }
    builder.delete(builder.length() - 2, builder.length());
    builder.append("]");
    return builder.toString();
  }

  public static String ausgabeToJson(Ausgabe ausgabe) {
    return "{\"grund\" : " + "\"" + ausgabe.getTitel() + "\"" + ", \"glaeubiger\" : " + "\""
        + ausgabe.getGlaeubiger() + "\"" + ", \"cent\" : " + "\"" + ausgabe.getSumme().multiply(100)
        .getNumber().intValue() + "\"" + ", \"schuldner\" : " + personListToJson(
        ausgabe.getSchuldnerListe()) + "}";
  }

  public static String ausgabenToJson(List<Ausgabe> ausgaben) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (int i = 0; i < ausgaben.size(); i++) {
      builder.append(ausgabeToJson(ausgaben.get(i)));
      if (!(i == ausgaben.size() - 1)) {
        builder.append(", ");
      }
    }
    builder.append("]");
    return builder.toString();
  }

  public static String gruppenListToJson(List<Gruppe> gruppen) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (int i = 0; i < gruppen.size(); i++) {
      builder.append(gruppeToJson(gruppen.get(i)));
      if (!(i == gruppen.size() - 1)) {
        builder.append(", ");
      }
    }
    builder.append("]");
    return builder.toString();
  }

  public static String schuldenToJson(Gruppe gruppe) {
    var map = gruppe.getSchulden();
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (Entry<String, Map<String, Money>> e : map.entrySet()) {
      for (Entry<String, Money> m : e.getValue().entrySet()) {
        if (!m.getValue().equals(Money.of(0, "EUR"))) {
          builder.append("{\"von\" : \"");
          builder.append(e.getKey());
          builder.append("\", \"an\" : \"");
          builder.append(m.getKey());
          builder.append("\", \"cents\" : ");
          builder.append(m.getValue().getNumber().doubleValue() * 100);
          builder.append("},");
        }
      }
    }
    builder.delete(builder.length() - 1, builder.length());
    builder.append("]");
    return builder.toString();

  }
}

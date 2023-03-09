package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Ausgabe;
import com.gruppe43.moneymanager.domain.Gruppe;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.javamoney.moneta.Money;

public class Serializer {
  public static String gruppeToJson(Gruppe gruppe) {
    StringBuilder builder = new StringBuilder();
    builder.append("{\"gruppe\" : ");
    builder.append("\"");
    builder.append(gruppe.getId());
    builder.append("\"");
    builder.append(", \"name\" : ");
    builder.append("\"");
    builder.append(gruppe.getTitel());
    builder.append("\"");
    builder.append(", \"personen\" : ");
    builder.append(gruppe.getTeilnehmer());
    builder.append(", \"geschlossen\" : ");
    builder.append(gruppe.isClosed());
    builder.append(", \"ausgaben\" : ");
    builder.append(ausgabenToJson(gruppe.getAusgaben()));
    builder.append("}");
    return builder.toString();
  }

  public static String ausgabeToJson(Ausgabe ausgabe) {
    StringBuilder builder = new StringBuilder();
    builder.append("{\"grund\" : ");
    builder.append("\"");
    builder.append(ausgabe.getTitel());
    builder.append("\"");
    builder.append(", \"glaeubiger\" : ");
    builder.append("\"");
    builder.append(ausgabe.getGlaeubiger());
    builder.append("\"");
    builder.append(", \"cent\" : ");
    builder.append("\"");
    builder.append(ausgabe.getSumme().multiply(100));
    builder.append("\"");
    builder.append(", \"schuldner\" : ");
    builder.append(ausgabe.getSchuldnerListe());
    builder.append("}");
    return builder.toString();
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
    for(Entry<String, Map<String, Money>> e : map.entrySet()) {
      for(Entry<String, Money> m : e.getValue().entrySet()) {
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

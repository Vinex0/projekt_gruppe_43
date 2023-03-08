package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Ausgabe;
import com.gruppe43.moneymanager.domain.Gruppe;
import java.util.List;

public class Serializer {
  public static String groupToJson(Gruppe gruppe) {
    StringBuilder builder = new StringBuilder();
    builder.append("{\"gruppe\" : ");
    builder.append(gruppe.getId());
    builder.append(", \"name\" : ");
    builder.append(gruppe.getTitel());
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
    builder.append(ausgabe.getTitel());
    builder.append(", \"glaeubiger\" : ");
    builder.append(ausgabe.getGlaeubiger());
    builder.append(", \"cent\" : ");
    builder.append(ausgabe.getSumme().multiply(100));
    builder.append(", \"schuldner\" : ");
    builder.append(ausgabe.getSchuldnerListe());
    builder.append("}");
    return builder.toString();
  }
  public static String ausgabenToJson(List<Ausgabe> ausgaben) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (Ausgabe a : ausgaben) {
      builder.append(ausgabeToJson(a));
      builder.append(", ");
    }
    builder.append("]");
    return builder.toString();
  }
}

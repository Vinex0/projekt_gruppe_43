package com.gruppe43.moneymanager.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ausgabeDb {

  @Autowired
  JdbcTemplate jdbcTemplate;

  int id;
  int gruppe;
  String titel;
  double summe;

  ausgabeDb(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public int getTitel() {
    return jdbcTemplate.query("SELECT titel FROM ausgabe WHERE id = " + id, String.class);
  }
}

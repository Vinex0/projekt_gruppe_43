package com.gruppe43.moneymanager.database;


import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class schuldenDb {
  @Autowired
  JdbcTemplate jdbcTemplate;
  int id;
  int gruppenId;
  String schuldner;
  String glaeubiger;
  Double summe;



}

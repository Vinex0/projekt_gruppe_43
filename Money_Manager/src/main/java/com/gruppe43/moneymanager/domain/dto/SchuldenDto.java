package com.gruppe43.moneymanager.domain.dto;

import com.gruppe43.moneymanager.domain.Gruppe;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchuldenDto {
  private final String glaeubiger;
  private final String schuldner;

  private final double summe;

  public SchuldenDto(String glaeubiger, String schuldner, double summe) {
    this.glaeubiger = glaeubiger;
    this.schuldner = schuldner;
    this.summe = summe;
  };
}

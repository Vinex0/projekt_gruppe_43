package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {

  private String nutzerName;

  private final List<Gruppe> gruppen;

  public Person(String nutzerName) {
    this.nutzerName = nutzerName;
    this.gruppen = new ArrayList<Gruppe>();
  }

  void addGruppe(Gruppe gruppe) {
    gruppen.add(gruppe);
  }

  public String getNutzerName() {
    return nutzerName;
  }

  public List<Gruppe> getGruppen() {
    return gruppen;
  }

  public void setNutzerName(String nutzerName) {
    this.nutzerName = nutzerName;
  }

}

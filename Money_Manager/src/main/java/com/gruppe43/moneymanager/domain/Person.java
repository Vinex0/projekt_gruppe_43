package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Person implements Comparable<Person> {

  private final Integer id;

  private final String nutzerName;

  private final List<Gruppe> gruppen;

  public Person(String nutzerName) {
    this(null, nutzerName, new ArrayList<>());
  }

  public Person(Integer id, String nutzerName, List<Gruppe> gruppen) {
    this.id = id;
    this.nutzerName = nutzerName;
    this.gruppen = gruppen;
  }

  void addGruppe(Gruppe gruppe) {
    gruppen.add(gruppe);
  }



  @Override
  public int compareTo(Person other) {
    return nutzerName.compareTo(other.nutzerName);
  }
}

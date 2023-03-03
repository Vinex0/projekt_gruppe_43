package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Ausgabe;
import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.domain.GruppenActionController;
import com.gruppe43.moneymanager.domain.Person;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

@Service
public class GruppenService {

  private final GruppenRepository gruppenRepository;
  private final List<Gruppe> gruppen = new ArrayList<>();


  public GruppenService(GruppenRepository gruppenRepository) {
    this.gruppenRepository = gruppenRepository;
  }

  public GruppenActionController createGroup(String title, Person username) {

    return new GruppenActionController(title, username);
  }

  public void addGruppe(String title, Person ersteller) {
    Gruppe g = new Gruppe(title, new ArrayList<>(List.of(ersteller)), new ArrayList<Ausgabe>(),
        new HashMap<Person, Map<Person, Money>>());
    gruppen.add(g);
  }

  public Gruppe getGruppe(String title) {
    for (Gruppe g : gruppen) {
      if (g.title().equals(title)) {
        return g;
      }
    }
    throw new InvalidParameterException("Group does not exist");
  }

  public List<String> getTitles(){
    return gruppen.stream().map(Gruppe::title).collect(Collectors.toList());
  }

}

package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.domain.Person;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GruppenService {

  private final GruppenRepository gruppenRepository;

  private final List<Gruppe> gruppen = new ArrayList<>();

  public GruppenService(GruppenRepository gruppenRepository) {
    this.gruppenRepository = gruppenRepository;
  }

  public List<Gruppe> alleGruppen() {
    return gruppenRepository.findAll().stream().sorted().toList();
  }

  public Gruppe gruppe(int nr) {
    return gruppenRepository.findById(nr).orElseThrow(NichtVorhandenException::new);
  }

  public Gruppe gruppeHinzufuegen(String titel, Person person) {
    Gruppe gruppe = new Gruppe(titel, person);
    return gruppenRepository.save(gruppe);
  }

  public void addGruppe(String title, Person ersteller) {
    Gruppe g = new Gruppe(title, ersteller, gruppen.size());
    gruppen.add(g);
  }

  public Gruppe getGruppe(String title) {
    for (Gruppe g : gruppen) {
      if (g.getTitel().equals(title)) {
        return g;
      }
    }
    throw new InvalidParameterException("Group does not exist");
  }

  public ArrayList<CheckboxHelper> getCheckboxHelper(String title) {
    ArrayList<CheckboxHelper> checkboxHelpers = new ArrayList<>();
    Gruppe g = getGruppe(title);
    for (Person p : g.getTeilnehmer()) {
      CheckboxHelper checkboxHelper = new CheckboxHelper(p, false);
      checkboxHelpers.add(checkboxHelper);
    }
    return checkboxHelpers;
  }

  public List<String> getTitles() {
    return gruppen.stream().map(Gruppe::getTitel).collect(Collectors.toList());
  }

  public Gruppe getGroupbyID(int id) {
    for (Gruppe g : gruppen) {
      if (g.getId() == id) {
        return g;
      }
    }
    return null;
  }

  public List<Gruppe> getGruppenbyNutzer(Person person) {
    List<Gruppe> nutzerGruppen = new ArrayList<>();
    for (Gruppe g : gruppen) {
      if (g.getTeilnehmer().contains(person)) {
        nutzerGruppen.add(g);
      }
    }
    return nutzerGruppen;
  }
}

package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.database.GruppenRepository;
import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.exeptions.NichtVorhandenException;
import com.gruppe43.moneymanager.helper.CheckboxHelper;
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

  public Gruppe gruppeHinzufuegen(String titel, String person) {
    Gruppe gruppe = new Gruppe(titel, person);
    return gruppenRepository.save(gruppe);
  }

  public void addGruppe(String title, String ersteller) {
    Gruppe g = new Gruppe(title, ersteller, String.valueOf(gruppen.size()));
    gruppen.add(g);
  }

  public void addGruppe(Gruppe gr) {
    gruppen.add(gr);
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
    Gruppe g = getGruppeById(title);
    for (String p : g.getTeilnehmer()) {
      CheckboxHelper checkboxHelper = new CheckboxHelper(p, false);
      checkboxHelpers.add(checkboxHelper);
    }
    return checkboxHelpers;
  }

  public List<String> getTitles() {
    return gruppen.stream().map(Gruppe::getTitel).collect(Collectors.toList());
  }

  public Gruppe getGruppeById(String id) {
    for (Gruppe g : gruppen) {
      if (g.getId().equals(id)) {
        return g;
      }
    }
    return null;
  }

  public List<Gruppe> getGruppenByNutzer(String person) {
    List<Gruppe> nutzerGruppen = new ArrayList<>();
    for (Gruppe g : gruppen) {
      if (g.getTeilnehmer().contains(person)) {
        nutzerGruppen.add(g);
      }
    }
    return nutzerGruppen;
  }

}

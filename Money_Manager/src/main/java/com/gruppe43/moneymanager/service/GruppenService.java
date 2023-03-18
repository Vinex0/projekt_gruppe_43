package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.exceptions.NichtVorhandenException;
import com.gruppe43.moneymanager.helper.CheckboxHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

@Service
public class GruppenService {

  private final GruppenRepository gruppenRepository;

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

  public void ausgabeHinzufuegen(int id, String name, List<String> schuldenTeilnehmer,
      Money summe, String ausgabeTitel) {
    Gruppe gruppe = getGruppeById(id);
    gruppe.createAusgabe(name, schuldenTeilnehmer, summe, ausgabeTitel);
    gruppenRepository.save(gruppe);
  }

  public Gruppe getGruppeByTitle(String title) {
    return gruppenRepository.findAll().stream().filter(gruppe -> gruppe.getTitel().equals(title))
        .findFirst().orElseThrow(NichtVorhandenException::new);

  }

  public ArrayList<CheckboxHelper> getCheckboxHelper(int id) {
    ArrayList<CheckboxHelper> checkboxHelpers = new ArrayList<>();
    Gruppe g = getGruppeById(id);
    for (String p : g.getTeilnehmer()) {
      CheckboxHelper checkboxHelper = new CheckboxHelper(p, false);
      checkboxHelpers.add(checkboxHelper);
    }
    return checkboxHelpers;
  }


  public List<String> getTitles() {
    return gruppenRepository.findAll().stream().map(Gruppe::getTitel).collect(Collectors.toList());
  }
  public List<String> alleTeilnehmer(int id) {
    return getGruppeById(id).getTeilnehmer();
  }

  public boolean isClosed(int id) {
    return getGruppeById(id).isClosed();
  }

  public void close(int id) {
    Gruppe gruppe = getGruppeById(id);
    gruppe.close();
    gruppenRepository.save(gruppe);
  }


  public Gruppe getGruppeById(int id) {
    return gruppenRepository.findById(id).orElseThrow(NichtVorhandenException::new);
  }

  public List<Gruppe> getGruppenByNutzer(String person) {
    return gruppenRepository.findAll().stream()
        .filter(g -> g.getTeilnehmer().contains(person))
        .collect(
            Collectors.toList());
  }

  public void teilnehmerHinzufuegen(int id, String nutzername) {
    Gruppe gruppe = getGruppeById(id);
    gruppe.addTeilnehmer(nutzername);
    gruppenRepository.save(gruppe);
  }

}

package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.domain.Person;
import java.util.List;
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

  public Gruppe gruppeHinzufuegen(String titel, Person person) {
    Gruppe gruppe = new Gruppe(titel, person);
    return gruppenRepository.save(gruppe);
  }


}

package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.domain.GruppenActionController;
import com.gruppe43.moneymanager.domain.Person;
import org.springframework.stereotype.Service;

@Service
public class GruppenService {

  private final GruppenRepository gruppenRepository;


  public GruppenService(GruppenRepository gruppenRepository) {
    this.gruppenRepository = gruppenRepository;
  }

  public GruppenActionController createGroup(String title, Person username) {

    return new GruppenActionController(title, username);
  }

  public Gruppe getGruppe(String title) {
    return getGruppe(title);
  }

}

package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Gruppe;
import java.util.List;
import java.util.Optional;

public interface GruppenRepository {

  List<Gruppe> finaAll();

  Optional<Gruppe> findById(int id);

  Gruppe save(Gruppe gruppe);
}

package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GruppenRepositoryImpl implements GruppenRepository {

  @Override
  public List<Gruppe> finaAll() {
    return null;
  }

  @Override
  public Optional<Gruppe> findById(int id) {
    return Optional.empty();
  }

  @Override
  public Gruppe save(Gruppe gruppe) {
    return null;
  }
}
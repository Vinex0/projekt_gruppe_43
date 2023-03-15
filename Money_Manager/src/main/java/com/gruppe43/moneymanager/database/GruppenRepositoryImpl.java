package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GruppenRepositoryImpl implements GruppenRepository {

  private final SpringDataGruppenRepository repository;

  public GruppenRepositoryImpl(SpringDataGruppenRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Gruppe> findAll() {
    List<GruppeDb> all = repository.findAll();
    return all.stream().map(this::toGruppe).toList();
  }

  @Override
  public Optional<Gruppe> findById(int id) {
    return repository.findById(String.valueOf(id)).map(this::toGruppe);
  }

  @Override
  public Gruppe save(Gruppe gruppe) {
    GruppeDb dbObject = fromGruppeDb(gruppe);
    GruppeDb saved = repository.save(dbObject);
    return  toGruppe(saved);
  }


  private Gruppe toGruppe(GruppeDb gruppeDb) {
    Gruppe gruppe = new Gruppe(gruppeDb.id(), gruppeDb.startPerson(), gruppeDb.titel());

    String s = "SELECT person FROM teilnehmer WHERE (SELECT teilnehmer FROM gruppen_teilnehmer WHERE gruppe = id)";

    gruppeDb.teilnehmer().forEach(gruppe::addTeilnehmer);
    gruppeDb.
    return null;
  }

  private GruppeDb fromGruppeDb(Gruppe gruppe) {
    return null;
  }
}

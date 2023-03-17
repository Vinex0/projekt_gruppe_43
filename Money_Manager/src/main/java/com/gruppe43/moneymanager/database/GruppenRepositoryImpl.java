package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;


//TODO
@Repository
public class GruppenRepositoryImpl implements GruppenRepository {
  private final SpringDataGruppenRepository springDataGruppenRepository;

  public GruppenRepositoryImpl(SpringDataGruppenRepository springDataGruppenRepository) {
    this.springDataGruppenRepository = springDataGruppenRepository;
  }

  @Override
  public List<Gruppe> findAll() {
    List<GruppeDb> all = springDataGruppenRepository.findAll();
    return all.stream().map(this::toGruppe).toList();

  }

  @Override
  public Optional<Gruppe> findById(String id) {
    return springDataGruppenRepository.findById(id).map(this::toGruppe);
  }

  @Override
  public Gruppe save(Gruppe gruppe) {
    GruppeDb gruppeDb = fromGruppe(gruppe);
    GruppeDb saved = springDataGruppenRepository.save(gruppeDb);
    return toGruppe(saved);
  }

  private GruppeDb fromGruppe(Gruppe gruppe) {
    return null;
  }

  private Gruppe toGruppe(GruppeDb saved) {
    return null;
  }

}

package com.gruppe43.moneymanager.database;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SpringDataGruppenRepository extends CrudRepository<GruppeDb, String> {

  List<GruppeDb> findAll();

}

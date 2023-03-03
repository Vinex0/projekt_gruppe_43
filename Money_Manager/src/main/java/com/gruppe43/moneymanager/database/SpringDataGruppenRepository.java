package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Gruppe;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SpringDataGruppenRepository extends CrudRepository<Gruppe, Integer> {

  List<Gruppe> findAll();
}

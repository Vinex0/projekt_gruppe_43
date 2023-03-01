package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Person;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SpringDataPersonRepository extends CrudRepository<Person, Integer> {

  List<Person> findAll();
}

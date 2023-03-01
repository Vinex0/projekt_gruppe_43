package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Person;
import com.gruppe43.moneymanager.service.PersonRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

  @Override
  public List<Person> findAll() {
    return null;
  }

  @Override
  public Optional<Person> findById(int id) {
    return Optional.empty();
  }

  @Override
  public Person save() {
    return null;
  }
}

package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository {

  List<Person> findAll();

  Optional<Person> findById(int id);

  Person save();
}

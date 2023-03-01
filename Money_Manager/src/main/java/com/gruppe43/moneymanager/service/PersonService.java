package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Person;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonRepository repository;

  public PersonService(PersonRepository repository) {
    this.repository = repository;
  }

  public List<Person> allPersons() {
    return repository.findAll().stream().toList();
  }

}

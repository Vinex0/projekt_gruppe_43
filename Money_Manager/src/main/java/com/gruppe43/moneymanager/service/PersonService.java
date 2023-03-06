package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Person;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public List<Person> allePersonen() {
    return personRepository.findAll().stream().sorted().toList();
  }

  public Person person(int nr) {
    return personRepository.findById(nr).orElseThrow(NichtVorhandenException::new);
  }

  public void personHinzufuegen(int id, String text) {

  }

  public List<Person> allPersons() {
    return personRepository.findAll().stream().toList();
  }



}

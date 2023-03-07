package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Person;


public class CheckboxHelper {

  public Person person;
  public boolean checked;

  public CheckboxHelper(Person person, boolean checked) {
    this.person = person;
    this.checked = checked;
  }
}

package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {

  private String userName;
  private List<Group> groups;

  Person(String userName) {
    this.userName = userName;
    this.groups = new ArrayList<Group>();
  }

  void addGroup(Group group) {
    groups.add(group);
  }

  String getUserName() {
    return userName;
  }

  List<Group> getGroups() {
    return groups;
  }
}

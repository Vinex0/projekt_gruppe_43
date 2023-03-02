package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {

  private String userName;

  private List<Group> groups;

  public Person(String userName) {
    this.userName = userName;
    this.groups = new ArrayList<Group>();
  }

  void addGroup(Group group) {
    groups.add(group);
  }

  public String getUserName() {
    return userName;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}

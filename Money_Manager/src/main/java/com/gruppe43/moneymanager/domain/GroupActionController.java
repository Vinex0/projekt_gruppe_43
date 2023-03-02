package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;

public class GroupActionController {

  private final Group group;

  public GroupActionController(String title, Person creator) {
    this.group = new Group(title, new ArrayList<Person>(), new ArrayList<Expense>(),
        new HashMap<Person, Map<Person, Money>>());
    addUser(creator);
  }

  void addUser(Person newUser) {
    newUser.addGroup(group);
    group.participants().add(newUser);
    group.debts().put(newUser, new HashMap<Person, Money>());
    for (Person p : group.participants()) {
      if (!p.equals(newUser)) {
        group.debts().get(newUser).put(p, Money.of(0, "EUR"));
        group.debts().get(p).put(newUser, Money.of(0, "EUR"));
      }
    }

  }

  public List<Person> getOtherPeople(Person creditor) {
    List<Person> keys = new ArrayList<>(group.debts().get(creditor).keySet());
    return keys;
  }

  public Map<Person, Money> getCreditors(Person debtor) {
    if (group.debts().containsKey(debtor)) {
      return group.debts().get(debtor).entrySet().stream()
          .filter((v) -> v.getValue().isGreaterThan(Money.of(0, "EUR")))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    }
    return null;
  }

  public void createExpense(Person creditor, List<Person> debtors, Money amount, String title) {
    Money individualAmount = CalculationHelpers.paymentShare(amount, debtors.size());
    Expense e = new Expense(title, creditor, debtors, amount);
    group.expenses().add(e);
    for (Person p : debtors) {
      if(!p.equals(creditor)){
        Money tmp = group.debts().get(p).get(creditor);
        group.debts().get(p).put(creditor, tmp.add(individualAmount));
      }
    }
    List<Person> part = new ArrayList<>(debtors);
    adjustDebts(part);
  }

  private void adjustDebts(List<Person> involved) {
    for (Person p : involved) {
      for (Person b : involved) {
        if (!b.equals(p)) {
          var amountP = group.debts().get(p).get(b);
          var amountB = group.debts().get(b).get(p);
          var diff = CalculationHelpers.difference(amountP, amountB);
          var reverseDiff = CalculationHelpers.difference(amountB, amountP);
          if (diff.isGreaterThan(Money.of(0, "EUR"))) {
            group.debts().get(p).put(b, diff);
            group.debts().get(b).put(p, Money.of(0, "EUR"));
          } else {
            group.debts().get(p).put(b, Money.of(0, "EUR"));
            group.debts().get(b).put(p, reverseDiff);
          }
        }
      }
    }
  }

  public Group getGroup() {
    return group;
  }

  public List<Expense> getExpenses() {
    return group.expenses();
  }

}


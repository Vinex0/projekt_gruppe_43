import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;

public class GroupActionController {

  private final Group group;

  GroupActionController(String title, Person creator) {
    this.group = new Group(
        title,
        new ArrayList<Person>(),
        new ArrayList<Expense>(),
        new HashMap<Person, Map<Person, Money>>()
    );
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
      return group.debts()
          .get(debtor)
          .entrySet()
          .stream()
          .filter((v) -> v.getValue().isGreaterThan(Money.of(0, "EUR")))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    }
    return null;
  }

  public void createExpense(Person creditor, List<Person> debtors, Money amount, String title) {
    Money individualAmount = CalculationHelpers.paymentShare(amount, debtors.size()+1);
    Expense e = new Expense(title, creditor, debtors, amount);
    group.expenses().add(e);
    for (Person p : debtors) {
      group.debts().get(p).put(creditor, individualAmount);
    }
    List<Person> part = new ArrayList<>(debtors);
    part.add(creditor);
    adjustDebts(part);
  }

  private void adjustDebts(List<Person> involved) {
    for (Person p : involved) {
      for (Person b : involved) {
        if (!b.equals(p)) {
          var amount_p = group.debts().get(p).get(b);
          var amount_b = group.debts().get(b).get(p);
          var diff = CalculationHelpers.difference(amount_p, amount_b);
          var reverse_diff = CalculationHelpers.difference(amount_b, amount_p);
          if (diff.isGreaterThan(Money.of(0, "EUR"))) {
            group.debts().get(p).put(b, diff);
            group.debts().get(b).put(p, Money.of(0, "EUR"));
          }
          else {
            group.debts().get(p).put(b, Money.of(0, "EUR"));
            group.debts().get(b).put(p, reverse_diff);
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


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

  public void createExpense(Person creditor, List<Person> debtors, Money amount) {
    Money individualAmount = CalculationHelpers.paymentShare(amount, debtors.size()+1);
    for (Person p : debtors) {
      group.debts().get(p).put(creditor, individualAmount);
    }
  }

  public Group getGroup() {
    return group;
  }
}


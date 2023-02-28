import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

  public Set<Person> getOtherPeople(Person creditor) {
    return group.debts().get(creditor).keySet();
  }

  public Set<Person> getCreditors(Person debtor) {
    if (group.debts().containsKey(debtor)) {
      return group.debts()
          .get(debtor)
          .entrySet()
          .stream()
          .filter((v) -> v.getValue().isGreaterThan(Money.of(0, "EUR"))).map((e) -> e.getKey())
          .collect(
              Collectors.toSet());
    }
    return null;
  }

  public Group getGroup() {
    return group;
  }
}


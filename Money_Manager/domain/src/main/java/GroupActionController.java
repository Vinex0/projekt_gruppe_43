import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

  private void addUser(Person newUser){
    group.participants().add(newUser);
    group.debts().put(newUser, new HashMap<Person, Money>());
    for (Person p : group.participants()) {
      if(!p.equals(newUser)){
        group.debts().get(newUser).put(p, Money.of(0, "EUR"));
        group.debts().get(p).put(newUser, Money.of(0, "EUR"));
      }
    }
    /*
    for(Person p : group.participants()){
      if(!p.equals(newUser)){
        group.debts().get(p).put(newUser, Money.of(0, "EUR"));
      }
    }*/


  }
 }


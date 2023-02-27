import java.util.ArrayList;
import java.util.List;
import org.javamoney.moneta.Money;

public class Person{

  private String userName;
  private List<Group> groups;

  Person(String userName){
    this.userName = userName;
    this.groups = new ArrayList<Group>();
  }

  void addGroup(Group group) {
    groups.add(group);
  }

  List<Group> getGroups() {
    return groups;
  }
}

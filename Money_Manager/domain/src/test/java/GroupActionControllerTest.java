import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.util.Arrays.asList;

public class GroupActionControllerTest {


  @Test
  void initGroupTestTitleValid() {
    Group group = createGroup();
    assertThat(group.title()).isEqualTo("test");
  }


  @Test
  void initGroupTestParticipantsNotNullValid() {
    Group group = createGroup();
    assertThat(group.participants().get(0)).isNotNull();
  }


  @Test
  void initGroupTestParticipantsValid() {
    Group group = createGroup();
    assertThat(group.participants().get(0).getUserName()).isEqualTo("Peter");
  }

  @Test
  void initGroupTestAddUserParticipantsValid() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    assertThat(groupController.getGroup().participants()).containsExactlyElementsOf(
        List.of(pet, sus));
  }

  @Test
  void initGroupTestNewUserDebtsAddExistUserValid() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    assertThat(groupController.getOtherPeople(sus)).containsExactlyElementsOf(
        List.of(pet));
  }


  @Test
  void initGroupTestNewUserAddedToExistUserDebtsValid() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    assertThat(groupController.getOtherPeople(pet)).containsExactlyElementsOf(
        List.of(sus));
  }

  @Test
  void getCreditorsEmptyFilterTest() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    Person jens = new Person("Jens");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    groupController.addUser(jens);
    assertThat(groupController.getCreditors(pet)).isEmpty();
  }
  @Test
  void  createExpenseTest() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    groupController.createExpense(pet, groupController.getOtherPeople(pet), Money.of(100, "EUR"), "expense test");

    assertThat(groupController.getExpenses().get(0).title()).isEqualTo("expense test");
    assertThat(groupController.getExpenses().get(0).amount()).isEqualTo(Money.of(100, "EUR"));
  }

  @Test
  void getCreditorsTest() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    Person jens = new Person("Jens");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    groupController.addUser(jens);

    groupController.createExpense(pet, groupController.getOtherPeople(pet), Money.of(150, "EUR"), "new expense");
    assertAll(
        () -> assertThat(groupController.getCreditors(sus)).isEqualTo(Map.of(pet, Money.of(50, "EUR"))),
        () -> assertThat(groupController.getCreditors(jens)).isEqualTo(Map.of(pet, Money.of(50, "EUR")))
    );
  }

  @Test
  void creditAdjustmentTest() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);


    groupController.createExpense(pet, groupController.getOtherPeople(pet), Money.of(100, "EUR"), "new expense");
    groupController.createExpense(sus, groupController.getOtherPeople(sus), Money.of(50, "EUR"), "new expense 2");
    var x = groupController.getCreditors(sus).get(pet);
    var y = groupController.getCreditors(pet).get(sus);
    assertAll(
        () -> assertThat(groupController.getCreditors(sus).get(pet)).isEqualTo(Money.of(25, "EUR")),
        () -> assertThat(groupController.getCreditors(pet).get(sus)).isNull()
    );
  }

  private Group createGroup() {
    GroupActionController groupController = new GroupActionController("test",
        new Person("Peter"));
    return groupController.getGroup();
  }


}

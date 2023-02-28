import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
    assertThat(groupController.getGroup().debts().get(sus).keySet()).containsExactlyElementsOf(
        List.of(pet));
  }


  @Test
  void initGroupTestNewUserAddedToExistUserDebtsValid() {
    Person pet = new Person("Peter");
    Person sus = new Person("Susan");
    GroupActionController groupController = new GroupActionController("test", pet);
    groupController.addUser(sus);
    assertThat(groupController.getGroup().debts().get(pet).keySet()).containsExactlyElementsOf(
        List.of(sus));
  }

  private Group createGroup() {
    GroupActionController groupController = new GroupActionController("test",
        new Person("Peter"));
    return groupController.getGroup();
  }


}

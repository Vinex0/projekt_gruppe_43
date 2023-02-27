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
  void initGroupTestParticipantsValid() {
    Group group = createGroup();
    ArrayList<Group> a = new ArrayList<>(Arrays.asList(group));
    assertThat(group.participants().get(0)).isNotNull();
  }

  private Group createGroup() {
    GroupActionController groupController = new GroupActionController("test",
        new Person("Peter"));
    return groupController.getGroup();
  }

}

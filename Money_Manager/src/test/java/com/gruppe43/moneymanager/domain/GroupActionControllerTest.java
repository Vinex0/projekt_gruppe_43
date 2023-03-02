package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    void createExpenseTest() {
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

        groupController.createExpense(pet, groupController.getGroup().participants(), Money.of(150, "EUR"), "new expense");
        assertAll(
                () -> assertThat(groupController.getCreditors(sus)).isEqualTo(Map.of(pet, Money.of(50, "EUR"))),
                () -> assertThat(groupController.getCreditors(jens)).isEqualTo(Map.of(pet, Money.of(50, "EUR")))
        );
    }

    @Test
    void testSzenario_1() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");

        GroupActionController groupController = new GroupActionController("test", pet);
        groupController.addUser(sus);


        groupController.createExpense(pet, groupController.getGroup().participants(), Money.of(10, "EUR"), "expense1");
        groupController.createExpense(pet, groupController.getGroup().participants(), Money.of(20, "EUR"), "expense2");

        assertThat(groupController.getCreditors(sus)).isEqualTo(Map.of(pet, Money.of(15, "EUR")));

    }

    @Test
    void testSzenario_2() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");

        GroupActionController groupController = new GroupActionController("test", pet);
        groupController.addUser(sus);


        groupController.createExpense(pet, groupController.getGroup().participants(), Money.of(10, "EUR"), "expense1");
        groupController.createExpense(sus, groupController.getGroup().participants(), Money.of(20, "EUR"), "expense2");

        assertThat(groupController.getCreditors(pet)).isEqualTo(Map.of(sus, Money.of(5, "EUR")));

    }

    //TODO adjust createExpense
    @Test
    void testSzenario_3() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");

        GroupActionController groupController = new GroupActionController("test", pet);
        groupController.addUser(sus);

        groupController.createExpense(pet, groupController.getOtherPeople(pet), Money.of(20, "EUR"), "expense1");


        assertThat(groupController.getCreditors(sus)).isEqualTo(Map.of(pet, Money.of(20, "EUR")));

    }

    //TODO
    @Test
    @Disabled
    void testSzenario_4() {
        Person a = new Person("Peter");
        Person b = new Person("Susan");
        Person c = new Person("jens");
        GroupActionController groupController = new GroupActionController("test", a);
        groupController.addUser(b);
        groupController.addUser(c);
        groupController.createExpense(a, List.of(a, b), Money.of(10, "EUR"), "expense 1");
        groupController.createExpense(b, List.of(b, c), Money.of(10, "EUR"), "expense 2");
        groupController.createExpense(c, List.of(c, a), Money.of(10, "EUR"), "expense 3");
        groupController.closeGroup();

        assertAll(
                ()->assertThat(groupController.getDebts(a)).allMatch((m)->m.isEqualTo(Money.of(0,"EUR"))),
                ()->assertThat(groupController.getDebts(b)).allMatch((m)->m.isEqualTo(Money.of(0,"EUR"))),
                ()->assertThat(groupController.getDebts(c)).allMatch((m)->m.isEqualTo(Money.of(0,"EUR")))

        );
    }

    @Test
    @Disabled
    void testSzenario_5() {
        Person a = new Person("Peter");
        Person b = new Person("Susan");
        Person c = new Person("jens");
        GroupActionController groupController = new GroupActionController("test", a);
        groupController.addUser(b);
        groupController.addUser(c);
        groupController.createExpense(a, List.of(a, b, c), Money.of(60, "EUR"), "expense 1");
        groupController.createExpense(b, List.of(a, b, c), Money.of(30, "EUR"), "expense 2");
        groupController.createExpense(c, List.of(b, c), Money.of(100, "EUR"), "expense 3");

        assertAll(
                ()->assertThat(groupController.getDebts(b)).contains(Money.of(30,"EUR")),
                ()->assertThat(groupController.getDebts(b)).contains(Money.of(20,"EUR"))

        );
    }

    @Test
    void creditAdjustmentTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        GroupActionController groupController = new GroupActionController("test", pet);
        groupController.addUser(sus);


        groupController.createExpense(pet, groupController.getGroup().participants(), Money.of(100, "EUR"), "new expense");
        groupController.createExpense(sus, groupController.getGroup().participants(), Money.of(50, "EUR"), "new expense 2");
        var x = groupController.getCreditors(sus).get(pet);
        var y = groupController.getCreditors(pet).get(sus);
        assertAll(
                () -> assertThat(groupController.getCreditors(sus).get(pet)).isEqualTo(Money.of(25, "EUR")),
                () -> assertThat(groupController.getCreditors(pet).get(sus)).isNull()
        );
    }

    @Test
    void partialExpenseTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        Person jens = new Person("Jens");
        GroupActionController groupController = new GroupActionController("test", pet);
        groupController.addUser(sus);
        groupController.addUser(jens);
        groupController.createExpense(pet, List.of(sus), Money.of(100, "EUR"), "new expense");
        assertThat(groupController.getCreditors(jens)).isEmpty();
    }

    private Group createGroup() {
        GroupActionController groupController = new GroupActionController("test",
                new Person("Peter"));
        return groupController.getGroup();
    }


}

package com.gruppe43.moneymanager.domain;

import java.util.List;
import java.util.Map;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GruppenActionControllerTest {


    @Test
    void initGroupTestTitleValid() {
        Gruppe gruppe = createGroup();
        assertThat(gruppe.title()).isEqualTo("test");
    }


    @Test
    void initGroupTestParticipantsNotNullValid() {
        Gruppe gruppe = createGroup();
        assertThat(gruppe.teilnehmer().get(0)).isNotNull();
    }


    @Test
    void initGroupTestParticipantsValid() {
        Gruppe gruppe = createGroup();
        assertThat(gruppe.teilnehmer().get(0).getNutzerName()).isEqualTo("Peter");
    }

    @Test
    void initGroupTestAddUserParticipantsValid() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        GruppenActionController gruppenController = new GruppenActionController("test", pet);
        gruppenController.addNutzer(sus);
        assertThat(gruppenController.getGroup().teilnehmer()).containsExactlyElementsOf(
                List.of(pet, sus));
    }

    @Test
    void initGroupTestNewUserDebtsAddExistUserValid() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);
        assertThat(groupController.getAnderePersonen(sus)).containsExactlyElementsOf(
                List.of(pet));
    }


    @Test
    void initGroupTestNewUserAddedToExistUserDebtsValid() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);
        assertThat(groupController.getAnderePersonen(pet)).containsExactlyElementsOf(
                List.of(sus));
    }

    @Test
    void getCreditorsEmptyFilterTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        Person jens = new Person("Jens");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);
        groupController.addNutzer(jens);
        assertThat(groupController.getGlaeubiger(pet)).isEmpty();
    }

    @Test
    void createExpenseTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);
        groupController.createAusgabe(pet, groupController.getAnderePersonen(pet), Money.of(100, "EUR"), "expense test");

        assertThat(groupController.getExpenses().get(0).title()).isEqualTo("expense test");
        assertThat(groupController.getExpenses().get(0).summe()).isEqualTo(Money.of(100, "EUR"));
    }

    @Test
    void getCreditorsTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        Person jens = new Person("Jens");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);
        groupController.addNutzer(jens);

        groupController.createAusgabe(pet, groupController.getGroup().teilnehmer(), Money.of(150, "EUR"), "new expense");
        assertAll(
                () -> assertThat(groupController.getGlaeubiger(sus)).isEqualTo(Map.of(pet, Money.of(50, "EUR"))),
                () -> assertThat(groupController.getGlaeubiger(jens)).isEqualTo(Map.of(pet, Money.of(50, "EUR")))
        );
    }

    @Test
    void testSzenario_1() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");

        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);


        groupController.createAusgabe(pet, groupController.getGroup().teilnehmer(), Money.of(10, "EUR"), "expense1");
        groupController.createAusgabe(pet, groupController.getGroup().teilnehmer(), Money.of(20, "EUR"), "expense2");

        assertThat(groupController.getGlaeubiger(sus)).isEqualTo(Map.of(pet, Money.of(15, "EUR")));

    }

    @Test
    void testSzenario_2() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");

        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);


        groupController.createAusgabe(pet, groupController.getGroup().teilnehmer(), Money.of(10, "EUR"), "expense1");
        groupController.createAusgabe(sus, groupController.getGroup().teilnehmer(), Money.of(20, "EUR"), "expense2");

        assertThat(groupController.getGlaeubiger(pet)).isEqualTo(Map.of(sus, Money.of(5, "EUR")));

    }

    //TODO adjust createExpense
    @Test
    void testSzenario_3() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");

        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);

        groupController.createAusgabe(pet, groupController.getAnderePersonen(pet), Money.of(20, "EUR"), "expense1");


        assertThat(groupController.getGlaeubiger(sus)).isEqualTo(Map.of(pet, Money.of(20, "EUR")));

    }

    //TODO
    @Test
    @Disabled
    void testSzenario_4() {
        Person a = new Person("Peter");
        Person b = new Person("Susan");
        Person c = new Person("jens");
        GruppenActionController groupController = new GruppenActionController("test", a);
        groupController.addNutzer(b);
        groupController.addNutzer(c);
        groupController.createAusgabe(a, List.of(a, b), Money.of(10, "EUR"), "expense 1");
        groupController.createAusgabe(b, List.of(b, c), Money.of(10, "EUR"), "expense 2");
        groupController.createAusgabe(c, List.of(c, a), Money.of(10, "EUR"), "expense 3");
        groupController.closeGroup();

        assertAll(
                ()->assertThat(groupController.getSchulden(a)).allMatch((m)->m.isEqualTo(Money.of(0,"EUR"))),
                ()->assertThat(groupController.getSchulden(b)).allMatch((m)->m.isEqualTo(Money.of(0,"EUR"))),
                ()->assertThat(groupController.getSchulden(c)).allMatch((m)->m.isEqualTo(Money.of(0,"EUR")))

        );
    }

    @Test
    @Disabled
    void testSzenario_5() {
        Person a = new Person("Peter");
        Person b = new Person("Susan");
        Person c = new Person("jens");
        GruppenActionController groupController = new GruppenActionController("test", a);
        groupController.addNutzer(b);
        groupController.addNutzer(c);
        groupController.createAusgabe(a, List.of(a, b, c), Money.of(60, "EUR"), "expense 1");
        groupController.createAusgabe(b, List.of(a, b, c), Money.of(30, "EUR"), "expense 2");
        groupController.createAusgabe(c, List.of(b, c), Money.of(100, "EUR"), "expense 3");

        assertAll(
                ()->assertThat(groupController.getSchulden(b)).contains(Money.of(30,"EUR")),
                ()->assertThat(groupController.getSchulden(b)).contains(Money.of(20,"EUR"))

        );
    }

    @Test
    void creditAdjustmentTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);


        groupController.createAusgabe(pet, groupController.getGroup().teilnehmer(), Money.of(100, "EUR"), "new expense");
        groupController.createAusgabe(sus, groupController.getGroup().teilnehmer(), Money.of(50, "EUR"), "new expense 2");
        var x = groupController.getGlaeubiger(sus).get(pet);
        var y = groupController.getGlaeubiger(pet).get(sus);
        assertAll(
                () -> assertThat(groupController.getGlaeubiger(sus).get(pet)).isEqualTo(Money.of(25, "EUR")),
                () -> assertThat(groupController.getGlaeubiger(pet).get(sus)).isNull()
        );
    }

    @Test
    void partialExpenseTest() {
        Person pet = new Person("Peter");
        Person sus = new Person("Susan");
        Person jens = new Person("Jens");
        GruppenActionController groupController = new GruppenActionController("test", pet);
        groupController.addNutzer(sus);
        groupController.addNutzer(jens);
        groupController.createAusgabe(pet, List.of(sus), Money.of(100, "EUR"), "new expense");
        assertThat(groupController.getGlaeubiger(jens)).isEmpty();
    }

    private Gruppe createGroup() {
        GruppenActionController groupController = new GruppenActionController("test",
                new Person("Peter"));
        return groupController.getGroup();
    }


}

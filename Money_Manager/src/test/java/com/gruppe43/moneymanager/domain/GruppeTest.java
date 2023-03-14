package com.gruppe43.moneymanager.domain;

import java.util.List;
import java.util.Map;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GruppeTest {

    @Test
    void initGruppeValidierung() {
        Gruppe gruppe = createGroup();
        assertAll(
            () -> assertThat(gruppe.getTitel()).isEqualTo("test"),
            () -> assertThat(gruppe.getStartPerson()).isEqualTo("Peter"),
            () -> assertThat(gruppe.getTeilnehmer()).isNotEmpty(),
            () -> assertThat(gruppe.getSchulden()).isNotEmpty(),
            () -> assertThat(gruppe.getAusgaben()).isEmpty()
        );
    }

    @Test
    void initGruppeAddTeilnehmer() {
        Gruppe gruppe = createGroup();
        gruppe.addTeilnehmer("Susan");
        assertAll(
            () ->  assertThat(gruppe.getTeilnehmer()).hasSize(2),
            () ->  assertThat(gruppe.getSchulden()).hasSize(2),
            () ->  assertThat(gruppe.getTeilnehmer()).contains("Susan")
        );
    }

    @Test
    void initGruppeNoGlaeubiger() {
        Gruppe gruppe = new Gruppe("GruppeA", "personA");
        gruppe.addTeilnehmer("personB");
        gruppe.addTeilnehmer("personC");
        assertThat(gruppe.getGlaeubigerFrom("personA")).isEmpty();
    }

    @Test
    void initGruppeAddAusgabe() {

        Gruppe gruppe = new Gruppe("test", "personA");
        gruppe.addTeilnehmer("personB");
        gruppe.createAusgabe("personA", List.of("personA", "personB"), Money.of(100, "EUR"), "expense test");
        gruppe.close();
        assertAll(
            () -> assertThat(gruppe.getAusgaben()).hasSize(1),
            () -> assertThat(gruppe.getSchuldenFromTo("personB", "personA")).isEqualTo(Money.of(50, "EUR")),
            () -> assertThat(gruppe.getSchuldenFromTo("personA", "personB")).isEqualTo(Money.of(0, "EUR"))
        );
    }

    @Test
    void getCreditorsTest() {

        Gruppe gruppe = new Gruppe("test", "personA");

        gruppe.addTeilnehmer("personB");
        gruppe.addTeilnehmer("personC");

        gruppe.createAusgabe("personA", List.of("personB", "personC"), Money.of(300, "EUR"), "expense1");
        gruppe.createAusgabe("personB", List.of("personC"), Money.of(300, "EUR"), "expense2");
        assertAll(
                () -> assertThat(gruppe.getGlaeubigerFrom("personB")).isEqualTo(Map.of("personA", Money.of(150, "EUR"))),
                () -> assertThat(gruppe.getGlaeubigerFrom("personC"))
                    .containsExactlyInAnyOrderEntriesOf(Map.of(
                        "personA", Money.of(150, "EUR"),
                        "personB", Money.of(300, "EUR")
                        )
                    )
        );
    }

    @Test
    void testSzenario_1() {
        Gruppe gruppe = new Gruppe("test", "personA");
        gruppe.addTeilnehmer("personB");

        gruppe.createAusgabe("personA", gruppe.getTeilnehmer(), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe("personA", gruppe.getTeilnehmer(), Money.of(20, "EUR"), "expense2");

        gruppe.close();
        assertThat(gruppe.getGlaeubigerFrom("personB")).isEqualTo(Map.of("personA", Money.of(15, "EUR")));
    }

    @Test
    void testSzenario_2() {
        Gruppe gruppe = new Gruppe("test", "personA");
        gruppe.addTeilnehmer("personB");

        gruppe.createAusgabe("personA", gruppe.getTeilnehmer(), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe("personB", gruppe.getTeilnehmer(), Money.of(20, "EUR"), "expense2");
        gruppe.close();
        assertThat(gruppe.getGlaeubigerFrom("personA")).isEqualTo(Map.of("personB", Money.of(5, "EUR")));
    }

    @Test
    void testSzenario_3() {

        Gruppe gruppe = new Gruppe("test", "personA");
        gruppe.addTeilnehmer("personB");

        gruppe.createAusgabe("personA", List.of("personB"), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe("personA", gruppe.getTeilnehmer(), Money.of(20, "EUR"), "expense2");
        gruppe.close();
        assertThat(gruppe.getGlaeubigerFrom("personB")).isEqualTo(Map.of("personA", Money.of(20, "EUR")));
    }


    @Test
    void testSzenario_4() {
        Gruppe gruppe = new Gruppe("test", "personA");
        gruppe.addTeilnehmer("personB");
        gruppe.addTeilnehmer("personC");

        gruppe.createAusgabe("personA", List.of("personA", "personB"), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe("personB", List.of("personB", "personC"), Money.of(10, "EUR"), "expense2");
        gruppe.createAusgabe("personC", List.of("personC", "personA"), Money.of(10, "EUR"), "expense3");

        gruppe.close();
        assertAll(
                ()->assertThat(gruppe.getTotalSchuldenFrom("personA")).isEqualTo(Money.of(0, "EUR")),
                ()->assertThat(gruppe.getTotalSchuldenFrom("personB")).isEqualTo(Money.of(0, "EUR")),
                ()->assertThat(gruppe.getTotalSchuldenFrom("personC")).isEqualTo(Money.of(0, "EUR"))
        );
    }

    @Test
    void testSzenario_5() {

        Gruppe gruppe = new Gruppe("test", "anton");
        gruppe.addTeilnehmer("berta");
        gruppe.addTeilnehmer("christian");

        gruppe.createAusgabe("anton", List.of("anton", "berta", "christian"), Money.of(60, "EUR"), "expense1");
        gruppe.createAusgabe("berta", List.of("anton", "berta", "christian"), Money.of(30, "EUR"), "expense2");
        gruppe.createAusgabe("christian", List.of("berta", "christian"), Money.of(100, "EUR"), "expense3");

        gruppe.close();
        assertAll(
                ()->assertThat(gruppe.getSchulden().get("berta").get("anton")).isEqualTo(Money.of(30,"EUR")),
                ()->assertThat(gruppe.getSchulden().get("berta").get("christian")).isEqualTo(Money.of(20,"EUR"))
        );
    }

    @Test
    void testSzenario_6() {
        Gruppe gruppe = new Gruppe("test", "a");
        gruppe.addTeilnehmer("b");
        gruppe.addTeilnehmer("c");
        gruppe.addTeilnehmer("d");
        gruppe.addTeilnehmer("e");
        gruppe.addTeilnehmer("f");

        gruppe.createAusgabe("a", List.of("a", "b", "c", "d", "e", "f"), Money.of(564, "EUR"), "Hotelzimmer");
        gruppe.createAusgabe("b", List.of("b", "a"), Money.of(38.58, "EUR"), "Benzin (Hinweg)");
        gruppe.createAusgabe("b", List.of("b", "a", "d"), Money.of(38.58, "EUR"), "Benzin (Rückweg)");
        gruppe.createAusgabe("c", List.of("c", "e", "f"), Money.of(82.11, "EUR"), "Benzin");
        gruppe.createAusgabe("d", List.of("a", "b", "c", "d", "e", "f"), Money.of(96, "EUR"), "Städtetour");
        gruppe.createAusgabe("f", List.of("b", "e", "f"), Money.of(95.37, "EUR"), "Theatervorstellung");

        gruppe.close();
        assertAll(
            ()->assertThat(gruppe.getSchuldenFromTo("b","a")).isEqualTo(Money.of(96.78,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("c", "a")).isGreaterThanOrEqualTo(Money.of(55.26,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("d", "a")).isEqualTo(Money.of(26.86,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("e", "a")).isEqualTo(Money.of(169.16,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("f", "a")).isEqualTo(Money.of(73.79,"EUR"))
        );
    }

    @Test
    void testSzenario_7() {

        Gruppe gruppe = new Gruppe("test", "a");
        gruppe.addTeilnehmer("b");
        gruppe.addTeilnehmer("c");
        gruppe.addTeilnehmer("d");
        gruppe.addTeilnehmer("e");
        gruppe.addTeilnehmer("f");
        gruppe.addTeilnehmer("g");

        gruppe.createAusgabe("d", List.of("d","f"), Money.of(20, "EUR"), "ausgabe1");
        gruppe.createAusgabe("g", List.of("b"), Money.of(10, "EUR"), "ausgabe2");
        gruppe.createAusgabe("e", List.of("a", "c", "e"), Money.of(75, "EUR"), "ausgabe3");
        gruppe.createAusgabe("f", List.of("a", "f"), Money.of(50, "EUR"), "ausgabe4");
        gruppe.createAusgabe("e", List.of("d"), Money.of(40, "EUR"), "ausgabe5");
        gruppe.createAusgabe("f", List.of("b", "f"), Money.of(40, "EUR"), "ausgabe6");
        gruppe.createAusgabe("f", List.of("c"), Money.of(5, "EUR"), "ausgabe7");
        gruppe.createAusgabe("g", List.of("a"), Money.of(30, "EUR"), "ausgabe8");

        gruppe.close();
        assertAll(
            ()->assertThat(gruppe.getSchuldenFromTo("a", "f")).isEqualTo(Money.of(40,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("a", "g")).isEqualTo(Money.of(40,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("b", "e")).isEqualTo(Money.of(30,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("c", "e")).isEqualTo(Money.of(30,"EUR")),
            ()->assertThat(gruppe.getSchuldenFromTo("d", "e")).isEqualTo(Money.of(30,"EUR"))
        );
    }

    @Test
    void creditAdjustmentTest() {

        Gruppe gruppe = new Gruppe("test", "a");
        gruppe.addTeilnehmer("b");

        gruppe.createAusgabe("a", gruppe.getTeilnehmer(), Money.of(100, "EUR"), "new expense");
        gruppe.createAusgabe("b", gruppe.getTeilnehmer(), Money.of(50, "EUR"), "new expense 2");
        gruppe.close();
        assertAll(
                () -> assertThat(gruppe.getGlaeubigerFrom("b").get("a")).isEqualTo(Money.of(25, "EUR")),
                () -> assertThat(gruppe.getGlaeubigerFrom("a").get("b")).isNull()
        );
    }

    @Test
    void partialExpenseTest() {

        Gruppe gruppe = new Gruppe("test", "a");
        gruppe.addTeilnehmer("b");
        gruppe.addTeilnehmer("c");

        gruppe.createAusgabe("a", List.of("b"), Money.of(100, "EUR"), "new expense");
        gruppe.close();
        assertThat(gruppe.getGlaeubigerFrom("c")).isEmpty();
    }

    private Gruppe createGroup() {
        return new Gruppe("test","Peter");
    }


}

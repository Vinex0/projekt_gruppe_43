package com.gruppe43.moneymanager.domain;

import java.util.List;
import java.util.Map;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GruppeTest {

    @Test
    void initGruppeValidierung() {
        Gruppe gruppe = createGroup();
        assertAll(
            () -> assertThat(gruppe.getTitel()).isEqualTo("test"),
            () -> assertThat(gruppe.getStartPerson().getNutzerName()).isEqualTo("Peter"),
            () -> assertThat(gruppe.getTeilnehmer()).isNotEmpty(),
            () -> assertThat(gruppe.getSchulden()).isNotEmpty(),
            () -> assertThat(gruppe.getAusgaben()).isEmpty()
        );
    }

    @Test
    void initGruppeAddTeilnehmer() {
        Person person = new Person("Susan");
        Gruppe gruppe = createGroup();
        gruppe.addTeilnehmer(person);
        assertAll(
            () ->  assertThat(gruppe.getTeilnehmer()).hasSize(2),
            () ->  assertThat(gruppe.getSchulden()).hasSize(2),
            () ->  assertThat(gruppe.getTeilnehmer()).contains(person)
        );

    }

    @Test
    void initGruppeNoGlaeubiger() {
        Person personA = new Person("A");
        Person personB = new Person("B");
        Person personC = new Person("C");
        Gruppe gruppe = new Gruppe("GruppeA", personA);

        gruppe.addTeilnehmer(personB);
        gruppe.addTeilnehmer(personC);

        assertThat(gruppe.getGlaeubigerFrom(personA)).isEmpty();
    }

    @Test
    void initGruppeAddAusgabe() {
        Person personA = new Person("A");
        Person personB = new Person("B");
        Gruppe gruppe = new Gruppe("test", personA);
        gruppe.addTeilnehmer(personB);
        gruppe.createAusgabe(personA, List.of(personA, personB), Money.of(100, "EUR"), "expense test");

        assertAll(
            () -> assertThat(gruppe.getAusgaben()).hasSize(1),
            () -> assertThat(gruppe.getSchuldenFromTo(personB, personA)).isEqualTo(Money.of(50, "EUR")),
            () -> assertThat(gruppe.getSchuldenFromTo(personA, personB)).isEqualTo(Money.of(0, "EUR"))
        );
    }

    @Test
    void getCreditorsTest() {
        Person personA = new Person("A");
        Person personB = new Person("B");
        Person personC = new Person("C");
        Gruppe gruppe = new Gruppe("test", personA);

        gruppe.addTeilnehmer(personB);
        gruppe.addTeilnehmer(personC);

        gruppe.createAusgabe(personA, List.of(personB, personC), Money.of(300, "EUR"), "expense1");
        gruppe.createAusgabe(personB, List.of(personC), Money.of(300, "EUR"), "expense2");
        assertAll(
                () -> assertThat(gruppe.getGlaeubigerFrom(personB)).isEqualTo(Map.of(personA, Money.of(150, "EUR"))),
                () -> assertThat(gruppe.getGlaeubigerFrom(personC))
                    .containsExactlyInAnyOrderEntriesOf(Map.of(
                        personA, Money.of(150, "EUR"),
                        personB, Money.of(300, "EUR")
                        )
                    )
        );
    }

    @Test
    void testSzenario_1() {
        Person personA = new Person("A");
        Person personB = new Person("B");

        Gruppe gruppe = new Gruppe("test", personA);
        gruppe.addTeilnehmer(personB);

        gruppe.createAusgabe(personA, gruppe.getTeilnehmer(), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe(personA, gruppe.getTeilnehmer(), Money.of(20, "EUR"), "expense2");

        assertThat(gruppe.getGlaeubigerFrom(personB)).isEqualTo(Map.of(personA, Money.of(15, "EUR")));
    }

    @Test
    void testSzenario_2() {
        Person personA = new Person("A");
        Person personB = new Person("B");

        Gruppe gruppe = new Gruppe("test", personA);
        gruppe.addTeilnehmer(personB);

        gruppe.createAusgabe(personA, gruppe.getTeilnehmer(), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe(personB, gruppe.getTeilnehmer(), Money.of(20, "EUR"), "expense2");

        assertThat(gruppe.getGlaeubigerFrom(personA)).isEqualTo(Map.of(personB, Money.of(5, "EUR")));
    }

    @Test
    void testSzenario_3() {
        Person personA = new Person("A");
        Person personB = new Person("B");

        Gruppe gruppe = new Gruppe("test", personA);
        gruppe.addTeilnehmer(personB);

        gruppe.createAusgabe(personA, List.of(personB), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe(personA, gruppe.getTeilnehmer(), Money.of(20, "EUR"), "expense2");

        assertThat(gruppe.getGlaeubigerFrom(personB)).isEqualTo(Map.of(personA, Money.of(20, "EUR")));
    }

    //TODO Ring check
    @Test
    @Disabled
    void testSzenario_4() {
        Person personA = new Person("A");
        Person personB = new Person("B");
        Person personC = new Person("C");

        Gruppe gruppe = new Gruppe("test", personA);
        gruppe.addTeilnehmer(personB);
        gruppe.addTeilnehmer(personC);

        gruppe.createAusgabe(personA, List.of(personA, personB), Money.of(10, "EUR"), "expense1");
        gruppe.createAusgabe(personB, List.of(personB, personC), Money.of(10, "EUR"), "expense2");
        gruppe.createAusgabe(personC, List.of(personC, personA), Money.of(10, "EUR"), "expense3");

        assertAll(
                ()->assertThat(gruppe.getGlaeubigerFrom(personA)).isNull(),
                ()->assertThat(gruppe.getGlaeubigerFrom(personB)).isNull(),
                ()->assertThat(gruppe.getGlaeubigerFrom(personC)).isNull()
        );
    }

    @Test
    @Disabled
    void testSzenario_5() {
        Person a = new Person("Anton");
        Person b = new Person("Berta");
        Person c = new Person("Christian");

        Gruppe gruppe = new Gruppe("test", a);
        gruppe.addTeilnehmer(b);
        gruppe.addTeilnehmer(c);

        gruppe.createAusgabe(a, List.of(a, b, c), Money.of(60, "EUR"), "expense1");
        gruppe.createAusgabe(b, List.of(a, b, c), Money.of(30, "EUR"), "expense2");
        gruppe.createAusgabe(c, List.of(b, c), Money.of(100, "EUR"), "expense3");

        assertAll(
                ()->assertThat(gruppe.getSchulden().get(b).get(a)).isEqualTo(Money.of(30,"EUR")),
                ()->assertThat(gruppe.getSchulden().get(b).get(c)).isEqualTo(Money.of(20,"EUR"))
        );
    }

    @Test
    @Disabled
    void testSzenario_6() {
        Person a = new Person("A");
        Person b = new Person("B");
        Person c = new Person("C");
        Person d = new Person("D");
        Person e = new Person("E");
        Person f = new Person("F");

        Gruppe gruppe = new Gruppe("test", a);
        gruppe.addTeilnehmer(b);
        gruppe.addTeilnehmer(c);
        gruppe.addTeilnehmer(d);
        gruppe.addTeilnehmer(e);
        gruppe.addTeilnehmer(f);

        gruppe.createAusgabe(a, List.of(a, b, c, d, e, f), Money.of(564, "EUR"), "Hotelzimmer");
        gruppe.createAusgabe(b, List.of(b, a), Money.of(38.58, "EUR"), "Benzin (Hinweg)");
        gruppe.createAusgabe(b, List.of(b, a, d), Money.of(38.58, "EUR"), "Benzin (Rückweg)");
        gruppe.createAusgabe(c, List.of(c, e, f), Money.of(82.11, "EUR"), "Benzin");
        gruppe.createAusgabe(d, List.of(a, b, c, d, e, f), Money.of(96, "EUR"), "Städtetour");
        gruppe.createAusgabe(f, List.of(b, e, f), Money.of(95.37, "EUR"), "Theatervorstellung");

        assertAll(
            ()->assertThat(gruppe.getSchuldenFromTo(b,a)).isEqualTo(Money.of(96.78,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(c).get(a)).isEqualTo(Money.of(55.26,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(d).get(a)).isEqualTo(Money.of(26.86,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(e).get(a)).isEqualTo(Money.of(169.16,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(f).get(a)).isEqualTo(Money.of(73.79,"EUR"))
        );
    }

    @Test
    @Disabled
    void testSzenario_7() {
        Person a = new Person("A");
        Person b = new Person("B");
        Person c = new Person("C");
        Person d = new Person("D");
        Person e = new Person("E");
        Person f = new Person("F");
        Person g = new Person("G");

        Gruppe gruppe = new Gruppe("test", a);
        gruppe.addTeilnehmer(b);
        gruppe.addTeilnehmer(c);
        gruppe.addTeilnehmer(d);
        gruppe.addTeilnehmer(e);
        gruppe.addTeilnehmer(f);
        gruppe.addTeilnehmer(g);

        gruppe.createAusgabe(d, List.of(d,f), Money.of(20, "EUR"), "ausgabe1");
        gruppe.createAusgabe(g, List.of(b), Money.of(10, "EUR"), "ausgabe2");
        gruppe.createAusgabe(e, List.of(a, c, e), Money.of(75, "EUR"), "ausgabe3");
        gruppe.createAusgabe(f, List.of(a, f), Money.of(50, "EUR"), "ausgabe4");
        gruppe.createAusgabe(e, List.of(d), Money.of(40, "EUR"), "ausgabe5");
        gruppe.createAusgabe(f, List.of(b, f), Money.of(40, "EUR"), "ausgabe6");
        gruppe.createAusgabe(f, List.of(c), Money.of(5, "EUR"), "ausgabe7");
        gruppe.createAusgabe(g, List.of(a), Money.of(30, "EUR"), "ausgabe8");

        assertAll(
            ()->assertThat(gruppe.getSchulden().get(a).get(f)).isEqualTo(Money.of(40,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(a).get(g)).isEqualTo(Money.of(40,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(b).get(e)).isEqualTo(Money.of(30,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(c).get(e)).isEqualTo(Money.of(30,"EUR")),
            ()->assertThat(gruppe.getSchulden().get(d).get(e)).isEqualTo(Money.of(30,"EUR"))
        );
    }

    @Test
    void creditAdjustmentTest() {
        Person a = new Person("A");
        Person b = new Person("B");
        Gruppe gruppe = new Gruppe("test", a);
        gruppe.addTeilnehmer(b);

        gruppe.createAusgabe(a, gruppe.getTeilnehmer(), Money.of(100, "EUR"), "new expense");
        gruppe.createAusgabe(b, gruppe.getTeilnehmer(), Money.of(50, "EUR"), "new expense 2");

        assertAll(
                () -> assertThat(gruppe.getGlaeubigerFrom(b).get(a)).isEqualTo(Money.of(25, "EUR")),
                () -> assertThat(gruppe.getGlaeubigerFrom(a).get(b)).isNull()
        );
    }

    @Test
    void partialExpenseTest() {
        Person a = new Person("A");
        Person b = new Person("B");
        Person c = new Person("C");
        Gruppe gruppe = new Gruppe("test", a);
        gruppe.addTeilnehmer(b);
        gruppe.addTeilnehmer(c);

        gruppe.createAusgabe(a, List.of(b), Money.of(100, "EUR"), "new expense");
        assertThat(gruppe.getGlaeubigerFrom(c)).isEmpty();
    }

    private Gruppe createGroup() {
        return new Gruppe("test", new Person("Peter"));
    }


}

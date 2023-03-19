package com.gruppe43.moneymanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


import com.gruppe43.moneymanager.database.GruppenRepositoryImpl;
import com.gruppe43.moneymanager.database.SpringDataGruppenRepository;
import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.exceptions.NichtVorhandenException;
import java.util.List;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
public class ServiceTest {

  @Autowired
  SpringDataGruppenRepository springDataGruppenRepository;
  GruppenRepository gruppenRepository;


  @BeforeEach
  void init(){
    gruppenRepository = new GruppenRepositoryImpl(springDataGruppenRepository);
  }

  @Test
  @Disabled
  @DisplayName("Gruppe erstellen")
  void test_1() {
    GruppenService service = new GruppenService(gruppenRepository);
    service.gruppeHinzufuegen("titel", "person");
    assertThat(service.getGruppenByNutzer("person")).contains(new Gruppe("titel", "person", 1));
  }

  @Test
  @Disabled
  @DisplayName("Gruppe schliessen")
  void test_2() {
    GruppenService service = new GruppenService(gruppenRepository);
    service.gruppeHinzufuegen("gruppe", "name");
    service.teilnehmerHinzufuegen(1, "name1");
    service.ausgabeHinzufuegen(1,"name", List.of("name", "name1"), Money.of(20, "EUR"), "titel");
    service.close(1);
    assertThat(service.getGruppeById(1).isClosed()).isTrue();
  }

  @Test
  @Disabled
  @DisplayName("Ausgabe erstellen")
  void test_3() {
    GruppenService service = new GruppenService(gruppenRepository);
    Gruppe gruppe = service.gruppeHinzufuegen("gruppe", "person");
    gruppe.addTeilnehmer("person1");
    gruppe.createAusgabe("person", List.of("name", "name1"), Money.of(20, "EUR"), "titel");
    assertThat(gruppe.getAusgaben()).isNotEmpty();
  }

  @Test
  @Disabled
  @DisplayName("Teilnehmer hinzufuegen")
  void test_4() {
    GruppenService service = new GruppenService(gruppenRepository);
    service.gruppeHinzufuegen("gruppe", "name");
    service.teilnehmerHinzufuegen(1, "teilnehmer");
    assertThat(service.getGruppeById(1).getTeilnehmer()).contains("name", "teilnehmer");
  }

  @Test
  @Disabled
  @DisplayName("Gruppe nicht vorhanden")
  void test_5() {
    GruppenService service = new GruppenService(gruppenRepository);
    assertThat(service.alleGruppen()).isEmpty();
    assertThrows(NichtVorhandenException.class, ()-> service.getGruppeById(20));
  }
}

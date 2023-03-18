package com.gruppe43.moneymanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


import com.gruppe43.moneymanager.database.GruppenRepositoryImpl;
import com.gruppe43.moneymanager.database.SpringDataGruppenRepository;
import com.gruppe43.moneymanager.domain.Gruppe;
import org.junit.jupiter.api.BeforeEach;
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
  @DisplayName("Eie Gruppe kann hinzugefuegt werden")
  void test_1() {
    GruppenService service = new GruppenService(gruppenRepository);
    service.gruppeHinzufuegen("titel", "person");
    assertThat(service.getGruppenByNutzer("person")).contains(new Gruppe("titel", "person", 1));
  }
}

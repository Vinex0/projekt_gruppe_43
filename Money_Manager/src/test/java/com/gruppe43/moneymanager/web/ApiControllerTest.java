package com.gruppe43.moneymanager.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenService;
import java.util.List;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest
@ContextConfiguration(classes = ApiController.class)
@AutoConfigureMockMvc(addFilters = false)

public class ApiControllerTest {

  @Autowired
  MockMvc mvc;
  @MockBean
  GruppenService gruppenService;

  @Test
  void gruppeDoesntExistTest() throws Exception {
    when(gruppenService.getGruppeById("0")).thenReturn(null);
    mvc.perform(get("/api/gruppen/0")
            .param("id", "0"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getGruppeTest() throws Exception {
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    testGruppe.addTeilnehmer("Jens");
    testGruppe.createAusgabe("Peter",List.of("Jens"), Money.of(10, "EUR"), "TestAusgabe");
    String expectedResponse = "{\"gruppe\" : \"null\", \"name\" : \"TestGruppe\", \"personen\" : [\"Peter\", \"Jens\"], \"geschlossen\" : false, \"ausgaben\" : [{\"grund\" : \"TestAusgabe\", \"glaeubiger\" : \"Peter\", \"cent\" : 1000, \"schuldner\" : [\"Jens\"]}]}";
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(get("/api/gruppen/0")
            .param("id", "0"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedResponse));
  }

  @Test
  void getAusgleichTest() throws Exception {
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    testGruppe.addTeilnehmer("Jens");
    testGruppe.createAusgabe("Peter",List.of("Jens"), Money.of(10, "EUR"), "TestAusgabe");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    String expectedResponse = "[{\"von\" : \"Jens\", \"an\" : \"Peter\", \"cents\" : 1000}]";
    mvc.perform(get("/api/gruppen/0/ausgleich")
            .param("id", "0"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedResponse));
  }

  @Test
  void getGruppenByUserTest() throws Exception {
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    testGruppe.addTeilnehmer("Jens");
    when(gruppenService.getGruppenByNutzer("Peter")).thenReturn(List.of(testGruppe));
    String expectedResponse = "[{\"gruppe\" : \"null\", \"name\" : \"TestGruppe\", \"personen\" : [\"Peter\", \"Jens\"]}]";
    mvc.perform(get("/api/user/Peter/gruppen")
            .param("name", "Peter"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedResponse));

  }

  @Test
  void createGruppeBadInputTest() throws Exception {
    mvc.perform(post("/api/gruppen")
            .param("data", "ciasdhodvhndiobnh"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createGruppeTest() throws Exception {
    String body = "{\"name\" : \"Test Gruppe\", \"personen\" : [\"Peter\", \"Jens\", \"Moritz\"] }";
    when(gruppenService.getGruppeByTitle(anyString())).thenReturn(mock(Gruppe.class));
    when(gruppenService.getGruppeByTitle(anyString()).getId()).thenReturn("0");
    mvc.perform(post("/api/gruppen")
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(content().string("0"));
    verify(gruppenService).gruppeHinzufuegen("Test Gruppe", "Peter");
  }

  @Test
  void gruppeSchliessenTest() throws Exception{
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(post("/api/gruppen/0/schliessen")
            .param("id", "0"))
        .andExpect(status().isOk());
    assertThat(testGruppe.isClosed()).isTrue();
  }

  @Test

  void createAusgabeTest() throws Exception {
    String body = "{\"grund\": \"IntelliJ Ultimate Abo\", \"glaeubiger\": \"Peter\", \"cent\" : 5000, \"schuldner\" : [\"Jens\"]}";
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    testGruppe.addTeilnehmer("Jens");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(post("/api/gruppen/0/auslagen")
            .param("id", "0")
            .content(body))
        .andExpect(status().isCreated());
  }

}

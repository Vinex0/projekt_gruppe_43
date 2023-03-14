package com.gruppe43.moneymanager.web;

import static org.mockito.Mockito.when;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenService;
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

/*  @Test
  void getGruppeTest() throws Exception {
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(get("/api/gruppen/0")
        .param("id", "0"))
        .andExpect(status().isOk())
        .andExpect(content().string(Serializer.gruppeToJson(testGruppe)));
  }*/

  @Test
  void gruppeSchliessenTest() throws Exception{
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(post("/api/gruppen/0/schliessen")
            .param("id", "0"))
        .andExpect(status().isOk());
    assertThat(testGruppe.isClosed()).isTrue();
  }

  /*@Test
  void createAusgabeTest() throws Exception {
    Gruppe testGruppe = new Gruppe("TestGruppe", "Peter");
    String requestBody = "{\"grund\": \"IntelliJ Ultimate Abo\", \"glaeubiger\": \"Peter\", \"cent\" : 5000, \"schuldner\" : [\"Peter\", \"Jens\"]}";
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(post("/api/gruppen/0/auslagen")
        .param("id", "0")
        .param("data", ""))
        .andExpect(status().isCreated());
  }*/


}

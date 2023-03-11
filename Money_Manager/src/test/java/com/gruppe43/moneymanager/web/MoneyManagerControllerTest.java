package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.helper.WithMockOAuth2User;
import com.gruppe43.moneymanager.service.CheckboxHelper;
import com.gruppe43.moneymanager.service.GruppenService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = MoneyManagerController.class)
@AutoConfigureMockMvc(addFilters = false)

public class MoneyManagerControllerTest {

  @Autowired
  MockMvc mvc;
  @MockBean
  GruppenService gruppenService;

  @Test
  void startTest() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  void startPostTest() throws Exception {
    mvc.perform(post("/")).andExpect(status().isFound());
  }

  @Test
  @WithMockOAuth2User(login = "Peter")
  void gruppenOverviewTest() throws Exception {
    mvc.perform(get("/gruppenOverview"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("nutzername", "Peter"))
        .andExpect(model().attributeExists("gruppen"))
        .andExpect(model().attributeExists("gruppenObject"));
  }

  @Test
  void createGruppeGetTest() throws Exception {
    mvc.perform(get("/createGruppe"))
        .andExpect(status().isOk());
  }

  @Test
  void createGruppePostTest() throws Exception {
    mvc.perform(post("/createGruppe")
            .param("name", "TestGruppe")
            .sessionAttr("nutzername", "Peter"))
        .andExpect(redirectedUrl("gruppenOverview"))
        .andExpect(status().isFound());
  }

  @Test
  void createGruppeNameFilterTest() throws Exception {
    mvc.perform(post("/createGruppe")
        .param("name", "")
        .sessionAttr("nutzername", "Peter"))
        .andExpect(redirectedUrl("createGruppe"))
        .andExpect(status().isFound());
  }

  @Test
  void gruppenIdGetTest() throws Exception {
    Gruppe testGruppe = new Gruppe("test", "Peter");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(get("/gruppe/0"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("gruppe"));
    verify(gruppenService).getGruppeById("0");
  }


  @Test
  void addNutzerIdTest() throws Exception {
    Gruppe testGruppe = mock(Gruppe.class);
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(post("/addNutzer/0")
            .param("id", "0")
            .param("nutzername", "Vinex0"))
        .andExpect(status().isFound());
    verify(gruppenService).getGruppeById("0");
    verify(testGruppe).addTeilnehmer("Vinex0");
  }
  @Test
  void createAusgabeGetTest() throws Exception {
    Gruppe testGruppe = mock(Gruppe.class);
    CheckboxHelper helper = mock(CheckboxHelper.class);
    ArrayList<CheckboxHelper> helpers = new ArrayList<>();
    helpers.add(helper);
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    when(gruppenService.getCheckboxHelper("0")).thenReturn(helpers);

    mvc.perform(get("/createAusgabe/0")
        .param("id", "0"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("checkboxHelpers"))
        .andExpect(model().attributeExists("gruppe"));
    verify(gruppenService).getGruppeById("0");
    verify(gruppenService).getCheckboxHelper("0");
  }

  @Test
  void createAusgabePostTest() throws Exception {
    Gruppe testGruppe = mock(Gruppe.class);
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    List<String> schuldenTeilnehmer = new ArrayList<>();
    mvc.perform(post("/createAusgabe/0")
        .param("id", "0")
        .param("ausgabeTitel", "TestAusgabe")
        .param("name", "Peter")
        .param("summe", "10"))
        .andExpect(status().isFound());
    verify(testGruppe)
        .createAusgabe("Peter", schuldenTeilnehmer, Money.parse("10" + " EUR"), "TestAusgabe");

  }

  @Test
  void schliesseGruppeTest() throws Exception {
    Gruppe testGruppe = new Gruppe("Test", "Peter");
    when(gruppenService.getGruppeById("0")).thenReturn(testGruppe);
    mvc.perform(post("/schliesseGruppe/0").param("id", "0"))
        .andExpect(status().isFound());
    assertThat(testGruppe.isClosed()).isTrue();
  }
}

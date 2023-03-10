package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.service.GruppenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

  /*@Test
  void gruppenOverviewTest() throws Exception {

    mvc.perform(get("/gruppenOverview"))
        .andExpect(status().isOk());
  }*/
  @Test
  void createGruppe() throws Exception {
    mvc.perform(post("/createGruppe")
        .param("name", "TestGruppe")
        .sessionAttr("nutzername", "Peter"))
        .andExpect(status().isFound());
  }

}

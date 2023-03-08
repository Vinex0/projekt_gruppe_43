package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenService;
import com.gruppe43.moneymanager.service.PersonService;
import com.gruppe43.moneymanager.service.Serializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {
  private final PersonService personService;
  private final GruppenService gruppenService;

  public ApiController(PersonService service, GruppenService gruppenService) {
    this.personService = service;
    this.gruppenService = gruppenService;
  }

  @GetMapping("/api/gruppen/{id}")
      public String getGruppe(@PathVariable("id") int id) {
      Gruppe gruppe = gruppenService.getGroupbyID(id);
      return Serializer.gruppeToJson(gruppe);
  }

  /*@GetMapping("/api/gruppen/{id}/ausgleich")
  public String getSchulden() {

  }*/

  @GetMapping("/api/user/{name}/gruppen")
  public String getGruppenOfPerson(@PathVariable("name") String name) {
    return Serializer.gruppenListToJson(gruppenService.getGruppenbyNutzer(personService.getPerson(name)));
  }

 /* @PostMapping("/gruppen")
    public String createGruppe() {

  }
  @PostMapping("/api/gruppen/{id}/schliesen")
  public String closeGruppe() {

  }
  @PostMapping("/api/gruppen/{id}/auslagen")
  public String createAusgabe() {

  }
  */
}

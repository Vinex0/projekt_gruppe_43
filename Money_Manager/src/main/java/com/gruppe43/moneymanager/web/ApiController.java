package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenService;
import com.gruppe43.moneymanager.service.Serializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {

  private final GruppenService gruppenService;

  public ApiController(GruppenService gruppenService) {
    this.gruppenService = gruppenService;
  }

  @GetMapping("/api/gruppen/{id}")
  public String getGruppe(@PathVariable("id") int id) {
    Gruppe gruppe = gruppenService.getGroupbyID(id);
    return Serializer.gruppeToJson(gruppe);
  }

  @GetMapping("/api/gruppen/{id}/ausgleich")
  public String getSchulden(@PathVariable("id") int id) {
    return Serializer.schuldenToJson(gruppenService.getGroupbyID(id));
  }

  @GetMapping("/api/user/{name}/gruppen")
  public String getGruppenOfPerson(@PathVariable("name") String name) {
    return Serializer.gruppenListToJson(gruppenService.getGruppenByNutzer(name));
  }

  @PostMapping("/gruppen")
  public String createGruppe() {
    return null;

  }

  @PostMapping("/api/gruppen/{id}/schliessen")
  public ResponseEntity<?> closeGruppe(@PathVariable("id") int id) {
    if (gruppenService.getGroupbyID(id) != null){
      //todo
      return ResponseEntity.status(HttpStatus.OK).build();
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @PostMapping("/api/gruppen/{id}/auslagen")
  public String createAusgabe() {
    return null;

  }

}
